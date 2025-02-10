package com.abhishek.recipefinder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.recipefinder.model.ApiResponse
import com.abhishek.recipefinder.model.RecipeResponse
import com.abhishek.recipefinder.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

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
}
