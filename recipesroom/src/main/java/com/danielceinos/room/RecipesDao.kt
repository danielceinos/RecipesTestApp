package com.danielceinos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY uid ASC")
    fun getRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes ORDER BY uid ASC")
    fun getRecipesSync(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(recipes : List<Recipe>)

    @Query("DELETE FROM recipes")
    suspend fun deleteAll()

    @Query("UPDATE recipes SET fav = :fav WHERE uid = :uid")
    fun markFavorite(uid: Int, fav: Boolean)
}
