package com.abhishek.recipefinder.repository

import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepository(private val apiService: ApiService) {
    fun fetchAllRecipes(): Flow<ApiResponse<RecipeResponse>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.getAllRecipes() // ✅ Direct call since it's a suspend function
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error("Error: ${e.message}"))
        }
    }
}

