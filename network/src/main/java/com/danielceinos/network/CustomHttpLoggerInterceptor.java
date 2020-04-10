package com.danielceinos.network;

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class CustomHttpLoggerInterceptor implements Interceptor {
   private static final Charset UTF8 = Charset.forName("UTF-8");

   public enum Level {
      /** No logs. */
      NONE,
      /**
       * Logs request and response lines.
       *
       * <p>Example:
       * <pre>{@code
       * --> POST /greeting http/1.1 (3-byte body)
       *
       * <-- 200 OK (22ms, 6-byte body)
       * }</pre>
       */
      BASIC,
      /**
       * Logs request and response lines and their respective headers.
       *
       * <p>Example:
       * <pre>{@code
       * --> POST /greeting http/1.1
       * Host: example.com
       * Content-Type: plain/text
       * Content-Length: 3
       * --> END POST
       *
       * <-- 200 OK (22ms)
       * Content-Type: plain/text
       * Content-Length: 6
       * <-- END HTTP
       * }</pre>
       */
      HEADERS,
      /**
       * Logs request and response lines and their respective headers and bodies (if present).
       *
       * <p>Example:
       * <pre>{@code
       * --> POST /greeting http/1.1
       * Host: example.com
       * Content-Type: plain/text
       * Content-Length: 3
       *
       * Hi?
       * --> END POST
       *
       * <-- 200 OK (22ms)
       * Content-Type: plain/text
       * Content-Length: 6
       *
       * Hello!
       * <-- END HTTP
       * }</pre>
       */
      BODY
   }

   public interface Logger {
      void log(String message);

      /** A {@link okhttp3.logging.HttpLoggingInterceptor.Logger} defaults output appropriate for the current platform. */
//      CustomHttpLoggerInterceptor.Logger DEFAULT = message -> Platform.get().log(INFO, message, null);
   }

//   public CustomHttpLoggerInterceptor() {
//      this(CustomHttpLoggerInterceptor.Logger.DEFAULT);
//   }

   public CustomHttpLoggerInterceptor(CustomHttpLoggerInterceptor.Logger logger) {
      this.logger = logger;
   }

   private final CustomHttpLoggerInterceptor.Logger logger;

   private volatile Set<String> headersToRedact = Collections.emptySet();

   public void redactHeader(String name) {
      Set<String> newHeadersToRedact = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
      newHeadersToRedact.addAll(headersToRedact);
      newHeadersToRedact.add(name);
      headersToRedact = newHeadersToRedact;
   }

   private volatile CustomHttpLoggerInterceptor.Level level = CustomHttpLoggerInterceptor.Level.NONE;

   /** Change the level at which this interceptor logs. */
   public CustomHttpLoggerInterceptor setLevel(CustomHttpLoggerInterceptor.Level level) {
      if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
      this.level = level;
      return this;
   }

   public CustomHttpLoggerInterceptor.Level getLevel() {
      return level;
   }

   @Override public Response intercept(Chain chain) throws IOException {
      CustomHttpLoggerInterceptor.Level level = this.level;

      Request request = chain.request();
      if (level == CustomHttpLoggerInterceptor.Level.NONE) {
         return chain.proceed(request);
      }

      boolean logBody = level == CustomHttpLoggerInterceptor.Level.BODY;
      boolean logHeaders = logBody || level == CustomHttpLoggerInterceptor.Level.HEADERS;

      RequestBody requestBody = request.body();
      boolean hasRequestBody = requestBody != null;

      String msgToLog = " \n";

      Connection connection = chain.connection();
      String requestStartMessage = "--> "
         + request.method()
         + ' ' + request.url()
         + (connection != null ? " " + connection.protocol() : "");
      if (!logHeaders && hasRequestBody) {
         requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
      }
      msgToLog = msgToLog + requestStartMessage + "\n";

      if (logHeaders) {
         if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
               msgToLog = msgToLog + "Content-Type: " + requestBody.contentType() + "\n";
            }
            if (requestBody.contentLength() != -1) {
               msgToLog = msgToLog + "Content-Length: " + requestBody.contentLength() + "\n";
            }
         }

         Headers headers = request.headers();
         for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
               msgToLog = msgToLog + logHeader(headers, i);
            }
         }

         if (!logBody || !hasRequestBody) {
            msgToLog = msgToLog + "--> END " + request.method() + "\n";
         } else if (bodyHasUnknownEncoding(request.headers())) {
            msgToLog = msgToLog + "--> END " + request.method() + " (encoded body omitted)" + "\n";
         } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
               charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
               msgToLog = msgToLog + buffer.readString(charset) + "\n";
               msgToLog = msgToLog + "--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)" + "\n";
            } else {
               msgToLog = msgToLog + "--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)" + "\n";
            }
         }
      }

      logger.log(msgToLog);

      msgToLog = " \n";
      long startNs = System.nanoTime();
      Response response;
      try {
         response = chain.proceed(request);
      } catch (Exception e) {
         msgToLog = msgToLog + "<-- HTTP FAILED: " + e + "\n";
         logger.log(msgToLog);
         throw e;
      }
      long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

      ResponseBody responseBody = response.body();
      long contentLength = responseBody.contentLength();
      String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

      msgToLog = msgToLog + "<-- "
         + response.code()
         + (response.message().isEmpty() ? "" : ' ' + response.message())
         + ' ' + response.request().url()
         + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')' + "\n";


      if (logHeaders) {
         Headers headers = response.headers();
         for (int i = 0, count = headers.size(); i < count; i++) {
            msgToLog = msgToLog + logHeader(headers, i);
         }

         if (!logBody || !HttpHeaders.hasBody(response)) {
            msgToLog = msgToLog + "<-- END HTTP" + "\n";
         } else if (bodyHasUnknownEncoding(response.headers())) {
            msgToLog = msgToLog + "<-- END HTTP (encoded body omitted)" + "\n";
         } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();

            Long gzippedLength = null;
            if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
               gzippedLength = buffer.size();
               try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                  buffer = new Buffer();
                  buffer.writeAll(gzippedResponseBody);
               }
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
               charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
               msgToLog = msgToLog + "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)" + "\n";
               logger.log(msgToLog);

               return response;
            }

            if (contentLength != 0) {
               msgToLog = msgToLog + buffer.clone().readString(charset) + "\n";
            }

            if (gzippedLength != null) {
               msgToLog = msgToLog + "<-- END HTTP (" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte body)" + "\n";
            } else {
               msgToLog = msgToLog + "<-- END HTTP (" + buffer.size() + "-byte body)" + "\n";
            }
         }
      }

      logger.log(msgToLog);

      return response;
   }

   private String logHeader(Headers headers, int i) {
      String msgToLog = "";
      String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
      msgToLog = msgToLog + (headers.name(i) + ": " + value) + "\n";

      return msgToLog;
   }

   /**
    * Returns true if the body in question probably contains human readable text. Uses a small sample
    * of code points to detect unicode control characters commonly used in binary file signatures.
    */
   static boolean isPlaintext(Buffer buffer) {
      try {
         Buffer prefix = new Buffer();
         long byteCount = buffer.size() < 64 ? buffer.size() : 64;
         buffer.copyTo(prefix, 0, byteCount);
         for (int i = 0; i < 16; i++) {
            if (prefix.exhausted()) {
               break;
            }
            int codePoint = prefix.readUtf8CodePoint();
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
               return false;
            }
         }
         return true;
      } catch (EOFException e) {
         return false; // Truncated UTF-8 sequence.
      }
   }

   private static boolean bodyHasUnknownEncoding(Headers headers) {
      String contentEncoding = headers.get("Content-Encoding");
      return contentEncoding != null
         && !contentEncoding.equalsIgnoreCase("identity")
         && !contentEncoding.equalsIgnoreCase("gzip");
   }
}
