package com.danielceinos.notesdomain.flux

import com.danielceinos.notesdomain.Recipe
import com.hoopcarpool.fluxy.BaseAction
import com.hoopcarpool.fluxy.Result

data class FetchRecipesAction(val useOnlyCached: Boolean): BaseAction

data class RecipesFetchedAction(val page: Int, val result: Result<List<Recipe>>, val isCached: Boolean): BaseAction
