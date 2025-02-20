package com.abhishek.recipefinder.data

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun RecipeList(recipes: List<Recipe>, onClick: (Recipe) -> Unit) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe, onClick = { onClick(recipe) }) // Call separate UI for each item
        }
    }
}
