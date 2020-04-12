package com.danielceinos.room

import androidx.room.*


@Suppress("EqualsOrHashCode")
@Entity(tableName = "favoriteRecipe")
data class FavoriteRecipe(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "fav") val fav: Boolean
)

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favoriteRecipe ORDER BY uid ASC")
    fun getFavoriteRecipes(): List<FavoriteRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteRecipe: FavoriteRecipe)
}