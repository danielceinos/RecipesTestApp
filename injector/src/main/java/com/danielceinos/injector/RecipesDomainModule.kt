package com.danielceinos.injector

import com.danielceinos.notesdomain.flux.RecipesController
import com.danielceinos.notesdomain.flux.RecipesStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Kodein module that provides notes dependencies
 */
object RecipesDomainModule {
    fun create() = Kodein.Module("RecipesDomainModule", true) {
        bindStore { RecipesStore(instance()) }
        bind<RecipesController>() with singleton { RecipesController(instance(), instance()) }
    }
}
