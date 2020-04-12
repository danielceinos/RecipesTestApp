package com.danielceinos.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, FavoriteRecipe::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    abstract fun favoriteRecipesDao(): FavoriteRecipeDao

}
