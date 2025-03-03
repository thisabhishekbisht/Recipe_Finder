package com.abhishek.recipefinder.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhishek.recipefinder.data.ApiResponse
import com.abhishek.recipefinder.data.Recipe
import com.abhishek.recipefinder.data.RecipeResponse
import com.abhishek.recipefinder.repository.RecipeRepository
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
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class RecipeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(RecipeRepository::class.java)
    private val viewModel = RecipeViewModel(repository)

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
    fun `fetchAllRecipes updates recipeState with ApiResponse_Success`() = runTest {
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

        val recipeResponse = RecipeResponse(
            limit = 10,
            recipes = listOf(recipe1, recipe2),
            skip = 0,
            total = 2
        )
        val apiResponse = ApiResponse.Success(recipeResponse)
        `when`(repository.fetchAllRecipes()).thenReturn(flowOf(apiResponse))

        // Act
        viewModel.fetchAllRecipes()

        // Assert
        assertEquals(apiResponse, viewModel.recipeState.value)
    }

    @Test
    fun `updateSelectedRecipe updates selectedRecipe`() {
        // Arrange
        val recipe = Recipe(
            caloriesPerServing = 200,
            cookTimeMinutes = 30,
            cuisine = "Italian",
            difficulty = "Easy",
            id = 1,
            image = "image_url",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            instructions = listOf("Step 1", "Step 2"),
            mealType = listOf("Dinner"),
            name = "Test Recipe",
            prepTimeMinutes = 15,
            rating = 4.5,
            reviewCount = 10,
            servings = 4,
            tags = listOf("Tag 1", "Tag 2"),
            userId = 123
        )

        // Act
        viewModel.updateSelectedRecipe(recipe)

        // Assert
        assertEquals(recipe, viewModel.selectedRecipe)
    }
}
