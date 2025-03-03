package com.abhishek.recipefinder.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.Recipe
import com.abhishek.recipefinder.data.SearchRecipeResponse
import com.abhishek.recipefinder.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    private val searchRepository = mock(SearchRepository::class.java)
    private val searchViewModel = SearchViewModel(searchRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchRecipes updates searchResults with ApiResponse_Success`() = runTest {
        // Arrange
        val recipe1 = Recipe(
            caloriesPerServing = 200,
            cookTimeMinutes = 30,
            cuisine = "Italian",
            difficulty = "Easy",
            id = 1,
            image = "image_url_1",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            instructions = listOf("Step 1", "Step 2"),
            mealType = listOf("Dinner"),
            name = "Test Recipe 1",
            prepTimeMinutes = 15,
            rating = 4.5,
            reviewCount = 10,
            servings = 4,
            tags = listOf("Tag 1", "Tag 2"),
            userId = 123
        )

        val recipe2 = Recipe(
            caloriesPerServing = 250,
            cookTimeMinutes = 40,
            cuisine = "Mexican",
            difficulty = "Medium",
            id = 2,
            image = "image_url_2",
            ingredients = listOf("Ingredient A", "Ingredient B"),
            instructions = listOf("Step A", "Step B"),
            mealType = listOf("Lunch"),
            name = "Test Recipe 2",
            prepTimeMinutes = 20,
            rating = 4.8,
            reviewCount = 8,
            servings = 3,
            tags = listOf("Tag A", "Tag B"),
            userId = 456
        )

        val searchRecipeResponse = SearchRecipeResponse(
            limit = 10,
            recipes = listOf(recipe1, recipe2),
            skip = 0,
            total = 2
        )
        val apiResponse = ApiResponse.Success(searchRecipeResponse)
        `when`(searchRepository.searchRecipes("test query")).thenReturn(flowOf(apiResponse))

        // Act
        searchViewModel.searchRecipes("test query")

        // Assert
        assertEquals(apiResponse, searchViewModel.searchResults.value)
    }
}
