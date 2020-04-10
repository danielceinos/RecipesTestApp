package com.danielceinos.todolist.features.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    protected val viewData = MutableLiveData<T>()

    /**
     * Returns a live data to keep track on changes.
     */
    fun getLiveData(): LiveData<T> = viewData
}