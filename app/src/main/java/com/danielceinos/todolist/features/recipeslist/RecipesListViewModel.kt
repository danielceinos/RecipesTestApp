package com.danielceinos.todolist.features.recipeslist

import androidx.lifecycle.viewModelScope
import com.danielceinos.notesdomain.flux.FetchRecipesAction
import com.danielceinos.notesdomain.flux.MarkRecipeFavoriteAction
import com.danielceinos.notesdomain.flux.RecipesStore
import com.danielceinos.todolist.features.base.BaseViewModel
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val dispatcher: Dispatcher,
    private val recipesStore: RecipesStore
) : BaseViewModel<Result<RecipesListViewState>>() {

    init {
        viewModelScope.launch {
            recipesStore.flow()
                .map { it.recipes }
                .collect {
                    val result: Result<RecipesListViewState> = when (it) {
                        is Result.Success -> Result.Success(
                            RecipesListViewState(
                                recipes = it.value.map { recipe ->
                                    RecipesListViewState.Recipe(
                                        id = recipe.uid,
                                        title = recipe.title,
                                        fav = recipe.fav
                                    )
                                },
                                cached = recipesStore.state.isCached
                            )
                        )
                        is Result.Loading -> Result.Loading()
                        is Result.Failure -> Result.Failure()
                        is Result.Empty -> Result.Empty()
                    }

                    viewData.postValue(result)
                }
        }
    }

    fun loadRecipes() {
        dispatcher.dispatch(FetchRecipesAction(false))
    }

    fun markFavorite(uid: Int) {
        dispatcher.dispatch(MarkRecipeFavoriteAction(uid))
    }
}


data class RecipesListViewState(val recipes: List<Recipe>, val cached: Boolean) {
    data class Recipe(val id: Int, val title: String, val fav: Boolean)
}