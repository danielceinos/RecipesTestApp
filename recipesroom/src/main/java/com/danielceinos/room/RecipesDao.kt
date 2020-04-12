package com.danielceinos.room

import androidx.room.*

@Entity(tableName = "recipes")
data class Recipe(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String
) {

    @PrimaryKey
    var uid: Int = title.hashCode()
}

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY uid ASC")
    fun getRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<Recipe>)

}
