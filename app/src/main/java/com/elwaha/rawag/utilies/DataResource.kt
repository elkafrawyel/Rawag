package com.elwaha.rawag.utilies

sealed class DataResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataResource<T>()
    data class Error(val errorMessage: String) : DataResource<Nothing>()
}