package com.danielceinos.todolist.features.recipeslist

import androidx.lifecycle.viewModelScope
import com.danielceinos.notesdomain.flux.FetchRecipesAction
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
                                it.value.map {
                                    RecipesListViewState.Recipe(
                                        id = it.uid,
                                        title = it.title,
                                        fav = it.fav
                                    )
                                }
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
}


data class RecipesListViewState(val recipes: List<Recipe>) {
    data class Recipe(val id: Int, val title: String, val fav: Boolean)
}