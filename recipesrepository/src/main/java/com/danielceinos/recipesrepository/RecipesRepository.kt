package com.danielceinos.recipesrepository

import com.danielceinos.network.RecipesApi
import com.danielceinos.notesdomain.Recipe
import com.danielceinos.notesdomain.repository.RecipesRepository
import com.danielceinos.notesdomain.repository.RepositoryResult
import com.danielceinos.room.RecipesDao

class RecipesRepositoryImpl(
    private val recipesApi: RecipesApi,
    private val recipesDao: RecipesDao
) : RecipesRepository {

    override fun getCachedRecipes(): RepositoryResult<List<Recipe>> {
        val recipes = recipesDao.getRecipesSync().map { it.toDomain() }
        return if (recipes.isNotEmpty()) {
            RepositoryResult.Cached(recipes)
        } else {
            RepositoryResult.Failure(Exception("No items cached"))
        }
    }

    override suspend fun getRecipes(page: Int): RepositoryResult<List<Recipe>> =
        try {
            val recipes = recipesApi.getAllRecipes(page).recipes.map { it.toDao() }
            recipesDao.insertAll(recipes)
            RepositoryResult.Success(recipes.map { it.toDomain() })
        } catch (e: Exception) {
            val recipes = recipesDao.getRecipesSync().map { it.toDomain() }
            if (recipes.isNotEmpty()) {
                RepositoryResult.Cached(recipes, e)
            } else {
                RepositoryResult.Failure(e)
            }
        }

    override fun setRecipeFavorite(uid: Int, fav: Boolean) {
        recipesDao.markFavorite(uid, fav)
    }
}

