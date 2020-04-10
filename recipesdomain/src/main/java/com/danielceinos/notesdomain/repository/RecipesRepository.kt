package com.danielceinos.notesdomain.repository

import com.danielceinos.notesdomain.Recipe

interface RecipesRepository {

    fun getCachedRecipes(): RepositoryResult<List<Recipe>>

    suspend fun getRecipes(page: Int): RepositoryResult<List<Recipe>>

    fun setRecipeFavorite(uid: Int, fav: Boolean)
}

