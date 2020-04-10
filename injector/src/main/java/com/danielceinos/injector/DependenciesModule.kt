package com.danielceinos.injector

import com.danielceinos.network.RecipesNetworkModule
import com.danielceinos.network.interceptors.InterceptorsModule
import com.danielceinos.notesdomain.RecipesDomainModule
import com.danielceinos.recipesrepository.RecipesRepositoryModule
import org.kodein.di.Kodein

/**
 * Kodein module that provide network classes
 */
object DependenciesModule {
    fun create() = Kodein.Module("DependenciesModule", true) {
        import(RecipesRepositoryModule.create())
        import(RecipesNetworkModule.create())
        import(RecipesDomainModule.create())
    }
}
