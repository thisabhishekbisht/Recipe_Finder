package com.abhishek.recipefinder.data

class SearchRecipeResponse(
    val limit: Int,
    val recipes: List<Recipe>,
    val skip: Int,
    val total: Int
)