package com.abhishek.recipefinder.activity.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.abhishek.recipefinder.activity.ui.utils.CenteredCircularProgressIndicator
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeList
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.viewModel.SearchViewModel

@Composable
fun RecipeSearchScreen(searchViewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by searchViewModel.searchResults.collectAsState()
    val navController = rememberNavController()
    Column {
        CollapsibleToolbar(
            isVisible = true,
            onSearchQueryChanged = { query -> searchQuery = query },
            onSearch = { searchViewModel.searchRecipes(searchQuery) }
        )
        when (searchResults) {
            is ApiResponse.Loading -> CenteredCircularProgressIndicator()
            is ApiResponse.Success -> {
                val recipes =
                    (searchResults as ApiResponse.Success<SearchRecipeResponse>).data.recipes
                RecipeList(recipes = recipes, onClick = { recipe ->
                    val recipeId = recipe.id // Pass recipeId to the details screen
                    val recipeName = recipe.name
                    // Store the selected recipe in the shared ViewModel
                    navController.navigate("details/$recipeId/$recipeName")
                })
            }

            is ApiResponse.Error -> {
                val errorMsg = (searchResults as ApiResponse.Error).message
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
