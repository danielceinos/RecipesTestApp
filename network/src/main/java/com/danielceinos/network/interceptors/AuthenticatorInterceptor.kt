package com.danielceinos.network.interceptors

import com.danielceinos.notesdomain.flux.RecipesStore
import okhttp3.Interceptor
import okhttp3.Response
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

const val AUTHORIZATION_HEADER = "Authorization"

/**
 * Intercepts the requests and adds the authentication header.
 * If authentication header is expired, tries to refresh the token and retries the request that failed.
 */
class AuthenticatorInterceptor(override val kodein: Kodein) : Interceptor, KodeinAware {

    private val sessionStore: RecipesStore by instance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }

}
