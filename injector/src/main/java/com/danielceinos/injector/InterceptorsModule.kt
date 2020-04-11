package com.danielceinos.injector

import com.danielceinos.network.interceptors.AuthenticatorInterceptor
import okhttp3.Interceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton

/**
 * Kodein module that provide OkHttp [Interceptor]
 */
object InterceptorsModule {
    fun create() = Kodein.Module("interceptors", true) {
        bind() from setBinding<Interceptor>()

        bind<Interceptor>().inSet() with singleton { AuthenticatorInterceptor() }
    }
}
