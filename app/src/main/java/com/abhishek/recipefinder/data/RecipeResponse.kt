package com.abhishek.recipefinder.data

data class RecipeResponse(
    val limit: Int,
    val recipes: List<Recipe>,
    val skip: Int,
    val total: Int
)