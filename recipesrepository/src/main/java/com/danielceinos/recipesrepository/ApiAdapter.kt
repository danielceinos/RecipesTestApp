package com.danielceinos.recipesrepository

import com.danielceinos.network.Recipes.Recipe as NetworkRecipe
import com.danielceinos.notesdomain.Recipe as DomainRecipe
import com.danielceinos.room.Recipe as RoomRecipe

fun NetworkRecipe.toDao(): RoomRecipe =
    RoomRecipe(
        title = title,
        ingredients = ingredients,
        thumbnailUrl = thumbnail,
        fav = false
    )

fun RoomRecipe.toDomain(): DomainRecipe =
    DomainRecipe(
        uid = uid,
        title = title,
        thumbnailUrl = thumbnailUrl,
        ingredients = ingredients.split(","),
        fav = fav
    )
