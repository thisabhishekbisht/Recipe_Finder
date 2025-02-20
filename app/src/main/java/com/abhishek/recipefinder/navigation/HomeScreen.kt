package com.abhishek.recipefinder.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeList
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.viewModel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: RecipeViewModel) {
    val recipeState by viewModel.recipeState.collectAsState()

    // ✅ Fetch API data only once
    LaunchedEffect(Unit) {
        viewModel.fetchAllRecipes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (recipeState) {
                is ApiResponse.Loading -> CenteredCircularProgressIndicator()

                is ApiResponse.Success -> {
                    val recipes = (recipeState as ApiResponse.Success<RecipeResponse>).data.recipes
                    // Use RecipeList to render the list of recipes
                    RecipeList(recipes = recipes, onClick = { recipe ->
                        val recipeId = recipe.id // Pass recipeId to the details screen
                        val recipeName = recipe.name

                        // Store the selected recipe in the shared ViewModel
                        viewModel.updateSelectedRecipe(recipe)
                        navController.navigate("details/$recipeId/$recipeName")
                    })
                }

                is ApiResponse.Error -> {
                    val errorMsg = (recipeState as ApiResponse.Error).message
                    Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
@Composable
fun CenteredCircularProgressIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator( modifier = Modifier.size(100.dp)) // Adjust the size as needed)
    }
}