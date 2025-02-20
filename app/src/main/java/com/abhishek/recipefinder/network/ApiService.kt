package com.abhishek.recipefinder.network

import com.abhishek.recipefinder.data.RecipeResponse
import retrofit2.http.GET

interface ApiService {
    /*    @GET("/recipes") // Example endpoint
        fun getAllRecipes(): Call<RecipeResponse>*/

    @GET("/recipes")
    suspend fun getAllRecipes(): RecipeResponse // ✅ Correct for coroutines
}