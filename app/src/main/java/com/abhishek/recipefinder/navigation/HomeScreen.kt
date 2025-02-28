package com.abhishek.recipefinder.navigation

import RecipeItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.activity.ui.utils.CenteredCircularProgressIndicator
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.abhishek.recipefinder.viewModel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    searchViewModel: SearchViewModel
) {
    val recipeState by recipeViewModel.recipeState.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    var previousScrollIndex by remember { mutableStateOf(0) }
    var previousScrollOffset by remember { mutableStateOf(0) }

    // Fetch API data only once
    LaunchedEffect(Unit) {
        recipeViewModel.fetchAllRecipes()
    }

    // Detect scroll direction and update the visibility of the search bar
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset + listState.firstVisibleItemIndex }
            .collect { currentScrollOffset ->
                val currentIndex = listState.firstVisibleItemIndex
                val currentOffset = listState.firstVisibleItemScrollOffset

                if (currentIndex > previousScrollIndex || (currentIndex == previousScrollIndex && currentOffset > previousScrollOffset)) {
                    isSearchBarVisible = false
                } else if (currentIndex < previousScrollIndex || (currentIndex == previousScrollIndex && currentOffset < previousScrollOffset - 30)) {
                    isSearchBarVisible = true
                }

                previousScrollIndex = currentIndex
                previousScrollOffset = currentOffset
            }
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
            if (isSearchBarVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            isSearching = it.isNotEmpty()
                        },
                        placeholder = { Text("Search for recipes...") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { searchViewModel.searchRecipes(searchQuery) }
                    ) {
                        Text("Search")
                    }
                }
            }

            if (isSearching) {
                when (searchResults) {
                    is ApiResponse.Loading -> CenteredCircularProgressIndicator()
                    is ApiResponse.Success<*> -> {
                        val recipes =
                            (searchResults as ApiResponse.Success<SearchRecipeResponse>).data.recipes
                        LazyColumn(state = listState) {
                            items(recipes) { recipe ->
                                RecipeItem(
                                    recipe = recipe,
                                    onClick = {
                                        val recipeId = recipe.id
                                        val recipeName = recipe.name
                                        recipeViewModel.updateSelectedRecipe(recipe)
                                        navController.navigate("details/$recipeId/$recipeName")
                                    },
                                    onLongClick = {
                                        // Handle long click if needed
                                    }
                                )
                            }
                        }
                    }

                    is ApiResponse.Error -> {
                        val errorMsg = (searchResults as ApiResponse.Error).message
                        Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
                when (recipeState) {
                    is ApiResponse.Loading -> CenteredCircularProgressIndicator()
                    is ApiResponse.Success -> {
                        val recipes =
                            (recipeState as ApiResponse.Success<RecipeResponse>).data.recipes
                        LazyColumn(state = listState) {
                            items(recipes) { recipe ->
                                RecipeItem(
                                    recipe = recipe,
                                    onClick = {
                                        val recipeId = recipe.id
                                        val recipeName = recipe.name
                                        recipeViewModel.updateSelectedRecipe(recipe)
                                        navController.navigate("details/$recipeId/$recipeName")
                                    },
                                    onLongClick = {
                                        // Handle long click if needed
                                    }
                                )
                            }
                        }
                    }

                    is ApiResponse.Error -> {
                        val errorMsg = (recipeState as ApiResponse.Error).message
                        Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
