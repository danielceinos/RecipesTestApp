package com.danielceinos.injector

import org.kodein.di.Kodein

/**
 * Kodein module that provide network classes
 */
object DependenciesModule {
    fun create() = Kodein.Module("DependenciesModule", true) {
        import(RecipesRepositoryModule.create())
        import(RecipesNetworkModule.create())
        import(RecipesDomainModule.create())
        import(NetworkModule.create())
    }
}
