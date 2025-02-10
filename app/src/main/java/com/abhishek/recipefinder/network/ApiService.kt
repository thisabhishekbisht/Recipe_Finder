package com.abhishek.recipefinder.network

import com.abhishek.recipefinder.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    /*    @GET("/recipes") // Example endpoint
        fun getAllRecipes(): Call<RecipeResponse>*/

    @GET("/recipes")
    suspend fun getAllRecipes(): RecipeResponse // ✅ Correct for coroutines
}