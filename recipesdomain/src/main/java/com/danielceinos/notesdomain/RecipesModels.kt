package com.danielceinos.notesdomain

data class Recipe(
    val uid: Int,
    val title: String,
    val ingredients: List<String>,
    val thumbnailUrl: String,
    val fav: Boolean
)
