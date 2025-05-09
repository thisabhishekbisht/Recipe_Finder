package com.abhishek.recipefinder.data

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}
