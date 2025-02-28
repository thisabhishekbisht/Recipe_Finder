package com.abhishek.recipefinder.network

import com.abhishek.recipefinder.repository.RecipeRepository
import com.abhishek.recipefinder.repository.SearchRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://dummyjson.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun provideRecipeRepository(): RecipeRepository {
        return RecipeRepository(apiService)
    }

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(apiService)
    }
}

