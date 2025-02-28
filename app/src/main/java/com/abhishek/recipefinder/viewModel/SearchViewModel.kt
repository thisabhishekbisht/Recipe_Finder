package com.abhishek.recipefinder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _searchResults =
        MutableStateFlow<ApiResponse<SearchRecipeResponse>>(ApiResponse.Loading)
    val searchResults: StateFlow<ApiResponse<SearchRecipeResponse>> get() = _searchResults

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            searchRepository.searchRecipes(query).collect { response ->
                _searchResults.value = response
            }
        }
    }
}
