package com.danielceinos.notesdomain.flux

import com.danielceinos.notesdomain.Recipe
import com.danielceinos.notesdomain.repository.RecipesRepository
import com.danielceinos.notesdomain.repository.RepositoryResult
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import kotlinx.coroutines.*


class RecipesController(
    private val dispatcher: Dispatcher,
    private val recipesRepository: RecipesRepository
) {

    private val job: Job = SupervisorJob()
    private val supervisorScope = CoroutineScope(job + Dispatchers.IO)

    fun cancelJobs() {
        if (job.isActive) {
            job.cancelChildren()
        }
    }

    fun getRecipes(page: Int, recipes: List<Recipe>, useOnlyCached: Boolean) {
        supervisorScope.launch {
            val repositoryResult =
                if (useOnlyCached)
                    recipesRepository.getCachedRecipes()
                else
                    recipesRepository.getRecipes(page)

            val result: Result<List<Recipe>> = when (repositoryResult) {
                is RepositoryResult.Success -> Result.Success(recipes + repositoryResult.value)
                is RepositoryResult.Cached -> Result.Success(recipes + repositoryResult.value)
                is RepositoryResult.Failure -> Result.Failure(repositoryResult.e)
            }

            val pageLoaded = when (repositoryResult) {
                is RepositoryResult.Success -> page
                is RepositoryResult.Cached -> repositoryResult.value.size / 10
                is RepositoryResult.Failure -> page - 1
            }

            dispatcher.dispatch(
                RecipesFetchedAction(pageLoaded, result, repositoryResult is RepositoryResult.Cached)
            )
        }
    }
}