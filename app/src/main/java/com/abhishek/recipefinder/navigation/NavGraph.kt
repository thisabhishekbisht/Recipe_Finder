package com.abhishek.recipefinder.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.abhishek.recipefinder.factory.RecipeViewModelFactory
import com.abhishek.recipefinder.factory.SearchViewModelFactory
import com.abhishek.recipefinder.network.RetrofitInstance
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.abhishek.recipefinder.viewModel.SearchViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    // ✅ Provide repository instance
    val repository = RetrofitInstance.provideRecipeRepository()
    val searchRepository = RetrofitInstance.provideSearchRepository()

    // ✅ Create ViewModel using factory
    val recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(repository))
    val searchViewModel: SearchViewModel =
        viewModel(factory = SearchViewModelFactory(searchRepository))

    NavHost(
        navController = navController,
        startDestination = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) "home" else "splashScreen"
    ) {
        composable("splashScreen") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController, recipeViewModel, searchViewModel) }
        composable(
            "details/{recipeId}/{recipeName}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.IntType },
                navArgument("recipeName") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            val recipeName = backStackEntry.arguments?.getString("recipeName")
            DetailScreen(navController, recipeId, recipeName, viewModel = recipeViewModel)
        }
    }
}
