package com.danielceinos.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recipes(
    @Json(name = "href")
    val href: String,
    @Json(name = "results")
    val recipes: List<Recipe>,
    @Json(name = "title")
    val title: String,
    @Json(name = "version")
    val version: Double
) {
    @JsonClass(generateAdapter = true)
    data class Recipe(
        @Json(name = "href")
        val href: String,
        @Json(name = "ingredients")
        val ingredients: String,
        @Json(name = "thumbnail")
        val thumbnail: String,
        @Json(name = "title")
        val title: String
    )
}