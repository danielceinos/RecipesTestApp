package com.danielceinos.injector


import com.danielceinos.network.RecipesApi
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

/**
 * Kodein module that provide network classes
 */
object RecipesNetworkModule {
    fun create() = Kodein.Module("RecipesNetworkModule", true) {
        bind<RecipesApi>() with singleton { instance<Retrofit>().create(RecipesApi::class.java) }
    }
}
