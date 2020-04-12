package com.danielceinos.todolist.features.recipesdetail

import androidx.lifecycle.viewModelScope
import com.danielceinos.notesdomain.flux.MarkRecipeFavoriteAction
import com.danielceinos.notesdomain.flux.RecipesStore
import com.danielceinos.todolist.features.base.BaseViewModel
import com.danielceinos.todolist.features.recipesdetail.RecipeDetailViewModel.RecipeDetailViewState
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.hoopcarpool.fluxy.Result.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val dispatcher: Dispatcher,
    private val recipesStore: RecipesStore
) : BaseViewModel<Result<RecipeDetailViewState>>() {

    lateinit var fragmentArgs: DetailFragmentArgs
    fun setup(fragmentArgs: DetailFragmentArgs) {
        this.fragmentArgs = fragmentArgs

        viewModelScope.launch {
            recipesStore.flow()
                .map { it.recipes }
                .collect {
                    when (val recipes = it) {
                        is Success -> {
                            val recipe = recipes.value.find { it.uid == fragmentArgs.recipeid }!!
                            viewData.postValue(
                                Success(
                                    RecipeDetailViewState(
                                        title = recipe.title,
                                        imageUrl = recipe.thumbnailUrl,
                                        favorite = recipe.fav
                                    )
                                )
                            )
                        }
                        is Loading -> {
                        }
                        is Failure -> {
                        }
                        is Empty -> {
                        }
                    }
                }
        }
    }

    fun markFavoriteRecipe() {
        dispatcher.dispatch(MarkRecipeFavoriteAction(fragmentArgs.recipeid))
    }

    data class RecipeDetailViewState(val imageUrl: String, val title: String, val favorite: Boolean)
}