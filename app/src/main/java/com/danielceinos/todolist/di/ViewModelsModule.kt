package com.danielceinos.todolist.di

import com.danielceinos.todolist.features.recipeslist.RecipesListViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

object ViewModelsModule {
    fun create() = Kodein.Module("ViewModelsModule", true) {
        bindViewModel {
            RecipesListViewModel(instance(), instance())
        }
    }
}