package com.danielceinos.notesdomain.flux

import com.danielceinos.notesdomain.Recipe
import com.hoopcarpool.fluxy.FluxyStore
import com.hoopcarpool.fluxy.Result

data class RecipesState(
    val recipes: Result<List<Recipe>> = Result.Empty(),
    val isCached: Boolean = false,
    val pageLoaded: Int = 0
)

class RecipesStore(private val recipesController: RecipesController) : FluxyStore<RecipesState>() {
    override fun init() {
        reduce<FetchRecipesAction> {
            val recipes = (state.recipes as? Result.Success)?.value ?: emptyList()
            newState = state.copy(recipes = Result.Loading())
            recipesController.getRecipes(state.pageLoaded + 1, recipes, it.useOnlyCached)
        }

        reduce<RecipesFetchedAction> {
            newState = state.copy(recipes = it.result, pageLoaded = it.page)
        }
    }
}