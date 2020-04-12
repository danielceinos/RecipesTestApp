package com.danielceinos.recipesrepository

import com.danielceinos.network.RecipesApi
import com.danielceinos.notesdomain.Recipe
import com.danielceinos.notesdomain.repository.RecipesRepository
import com.danielceinos.notesdomain.repository.RepositoryResult
import com.danielceinos.room.FavoriteRecipe
import com.danielceinos.room.FavoriteRecipeDao
import com.danielceinos.room.RecipesDao

class RecipesRepositoryImpl(
    private val recipesApi: RecipesApi,
    private val recipesDao: RecipesDao,
    private val favoriteRecipeDao: FavoriteRecipeDao
) : RecipesRepository {

    override fun getCachedRecipes(): RepositoryResult<List<Recipe>> {
        val favoriteRecipes = favoriteRecipeDao.getFavoriteRecipes().map { it.uid to it }.toMap()
        val recipes = recipesDao.getRecipes().map { recipe -> domainRecipeFrom(recipe, favoriteRecipes[recipe.uid]) }
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
            val favoriteRecipes = favoriteRecipeDao.getFavoriteRecipes().map { it.uid to it }.toMap()
            RepositoryResult.Success(recipes.map { recipe -> domainRecipeFrom(recipe, favoriteRecipes[recipe.uid]) })
        } catch (e: Exception) {
            val favoriteRecipes = favoriteRecipeDao.getFavoriteRecipes().map { it.uid to it }.toMap()
            val recipes = recipesDao.getRecipes().map { recipe -> domainRecipeFrom(recipe, favoriteRecipes[recipe.uid]) }
            if (recipes.isNotEmpty()) {
                RepositoryResult.Cached(recipes, e)
            } else {
                RepositoryResult.Failure(e)
            }
        }

    override fun setRecipeFavorite(uid: Int, fav: Boolean) {
        favoriteRecipeDao.insert(FavoriteRecipe(uid, fav))
    }
}

