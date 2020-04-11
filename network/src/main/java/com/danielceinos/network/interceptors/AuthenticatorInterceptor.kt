package com.danielceinos.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

const val AUTHORIZATION_HEADER = "Authorization"

/**
 * Intercepts the requests and adds the authentication header.
 */
class AuthenticatorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }

}
