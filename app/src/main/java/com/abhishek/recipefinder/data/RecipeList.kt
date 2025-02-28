package com.abhishek.recipefinder.data

import RecipeDetailDialog
import RecipeItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RecipeList(recipes: List<Recipe>, onClick: (Recipe) -> Unit) {
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(
                recipe = recipe,
                onClick = { onClick(recipe) },
                onLongClick = { selectedRecipe = recipe }
            )
        }
    }

    selectedRecipe?.let { recipe ->
        RecipeDetailDialog(
            recipe = recipe,
            onDismissRequest = { selectedRecipe = null }
        )
    }
}
