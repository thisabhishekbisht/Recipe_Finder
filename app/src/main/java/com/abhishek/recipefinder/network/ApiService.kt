package com.abhishek.recipefinder.network

import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.data.SearchRecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/recipes")
    suspend fun getAllRecipes(): RecipeResponse // ✅ Correct for coroutines

    /*abstract fun searchRecipes(query: String): SearchRecipeResponse */ /*blocking calls. Bad for Android main thread*/

    @GET("recipes/search")
    suspend fun searchRecipes(@Query("q") query: String): SearchRecipeResponse

}