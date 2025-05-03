package com.abhishek.recipefinder.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.activity.composables.RecipeList
import com.abhishek.recipefinder.activity.ui.utils.CenteredCircularProgressIndicator
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.abhishek.recipefinder.viewModel.SearchViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    searchViewModel: SearchViewModel
) {
    val recipeState by recipeViewModel.recipeState.collectAsState()

    LaunchedEffect(Unit) {
        recipeViewModel.fetchAllRecipes()
    }

    Column {

        when (recipeState) {
            is ApiResponse.Loading -> CenteredCircularProgressIndicator()
            is ApiResponse.Success -> {
                val recipes = (recipeState as ApiResponse.Success<RecipeResponse>).data.recipes
                RecipeList(recipes = recipes, onClick = { recipe ->
                    val recipeId = recipe.id
                    val recipeName = recipe.name
                    recipeViewModel.updateSelectedRecipe(recipe)
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.activity.ui.utils.CenteredCircularProgressIndicator
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.abhishek.recipefinder.viewModel.SearchViewModel

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

    LaunchedEffect(Unit) {
        recipeViewModel.fetchAllRecipes()
    }

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

    Scaffold { innerPadding ->
        val seamlessColor = Color.Gray

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isSearchBarVisible) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = seamlessColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                isSearching = it.isNotEmpty()
                            },
                            placeholder = { Text("Search for recipes") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            /*      colors = TextFieldDefaults.textFieldColors(
                                      backgroundColor = Color.Transparent,
                                      focusedIndicatorColor = Color.Transparent,
                                      unfocusedIndicatorColor = Color.Transparent,
                                      cursorColor = Color.Black, // Adjust cursor color if necessary
                                      textColor = Color.Black // Adjust text color if necessary
                                  ),*/
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                searchViewModel.searchRecipes(searchQuery)
                            })
                        )
                        IconButton(onClick = { /* Handle microphone click */ }) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null
                            )
                        }
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
