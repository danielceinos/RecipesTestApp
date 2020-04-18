package com.danielceinos.recipesrepository

import com.danielceinos.network.Recipes.Recipe as NetworkRecipe
import com.danielceinos.notesdomain.Recipe as DomainRecipe
import com.danielceinos.room.FavoriteRecipe as RoomFavoriteRecipe
import com.danielceinos.room.Recipe as RoomRecipe

fun NetworkRecipe.toDao(): RoomRecipe =
    RoomRecipe(
        title = title,
        ingredients = ingredients,
        thumbnailUrl = thumbnail
    )

fun domainRecipeFrom(
    roomRecipe: RoomRecipe,
    roomFavoriteRecipe: RoomFavoriteRecipe?
) =
    DomainRecipe(
        uid = roomRecipe.uid,
        title = roomRecipe.title,
        thumbnailUrl = roomRecipe.thumbnailUrl,
        ingredients = roomRecipe.ingredients.split(",").map { it.trim() },
        fav = roomFavoriteRecipe?.fav ?: false
    )
