package com.abhishek.recipefinder.repository

import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository(private val apiService: ApiService) {
    fun searchRecipes(query: String): Flow<ApiResponse<SearchRecipeResponse>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response =
                apiService.searchRecipes(query) // Direct call since it's a suspend function
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error("Error: ${e.message}"))
        }
    }
}
