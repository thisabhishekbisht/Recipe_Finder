package com.abhishek.recipefinder.ui.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.abhishek.recipefinder.model.Recipe

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe) // Call separate UI for each item
        }
    }
}
