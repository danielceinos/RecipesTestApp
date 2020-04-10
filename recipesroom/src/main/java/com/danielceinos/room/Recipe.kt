package com.danielceinos.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Suppress("EqualsOrHashCode")
@Entity(tableName = "recipes")
data class Recipe(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String,
    @ColumnInfo(name = "fav") val fav: Boolean
) {

    @PrimaryKey
    var uid: Int = title.hashCode()
}

