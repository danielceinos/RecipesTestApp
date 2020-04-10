package com.danielceinos.notesdomain.repository

sealed class RepositoryResult<T> {
    data class Success<T>(val value: T) : RepositoryResult<T>()
    data class Failure<T>(val e: Exception) : RepositoryResult<T>()
    data class Cached<T>(val value: T, val e: Exception? = null) : RepositoryResult<T>()
}