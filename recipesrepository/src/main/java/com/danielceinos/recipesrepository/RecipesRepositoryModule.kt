package com.danielceinos.recipesrepository

import com.danielceinos.notesdomain.repository.RecipesRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object RecipesRepositoryModule {
    fun create() = Kodein.Module("RecipesRepositoryModule", true) {
        bind<RecipesRepository>() with singleton { RecipesRepositoryImpl(instance(), instance()) }
    }
}