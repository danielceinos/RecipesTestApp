package com.danielceinos.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApi {

    @GET("api")
    suspend fun getAllRecipes(@Query("p") page: Int): Recipes
}