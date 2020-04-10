package com.danielceinos.network

import com.danielceinos.common.AppLogger
import com.danielceinos.network.interceptors.InterceptorsModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Kodein module that provide network classes
 */
object NetworkModule {
    fun create() = Kodein.Module("NetworkModule", true) {
        import(InterceptorsModule.create())
        bind<OkHttpClient>() with singleton {
            val isDebug = instance<Boolean>("debug")

            val client = getOkHttpBuilder(10)

            val interceptor = CustomHttpLoggerInterceptor {
                instance<AppLogger>().d("OkHttpClient", it)
            }.setLevel(
                if (isDebug) CustomHttpLoggerInterceptor.Level.BODY else CustomHttpLoggerInterceptor.Level.NONE
            )
            val interceptors = instance<Set<Interceptor>>()

            interceptors.forEach {
                client.addNetworkInterceptor(it)
            }
            client.addNetworkInterceptor(interceptor)
            client.build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .baseUrl(instance<String>("baseUrl"))
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<Moshi>() with singleton {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }
    }

    private fun getOkHttpBuilder(timeoutSeconds: Long) =
        OkHttpClient.Builder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
}
