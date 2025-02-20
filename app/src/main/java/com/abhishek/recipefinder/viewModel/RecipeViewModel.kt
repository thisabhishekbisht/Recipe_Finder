package com.abhishek.recipefinder.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.Recipe
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    // State to hold the selected recipe. Using mutableStateOf allows Compose to react to changes.
    private val _selectedRecipe = mutableStateOf<Recipe?>(null)
    val selectedRecipe get() = _selectedRecipe.value
    private val _recipeState = MutableStateFlow<ApiResponse<RecipeResponse>>(ApiResponse.Loading)
    val recipeState: StateFlow<ApiResponse<RecipeResponse>> = _recipeState

    // Fetch recipes from the repository
    fun fetchAllRecipes() {
        viewModelScope.launch {
            repository.fetchAllRecipes().collectLatest { response ->
                _recipeState.value = response
            }
        }
    }

    fun updateSelectedRecipe(recipe: Recipe) {
        _selectedRecipe.value=recipe
    }
}