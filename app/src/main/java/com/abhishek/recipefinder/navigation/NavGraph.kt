package com.abhishek.recipefinder.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhishek.recipefinder.factory.LoginViewModelFactory

import com.abhishek.recipefinder.factory.RecipeViewModelFactory
import com.abhishek.recipefinder.factory.SearchViewModelFactory
import com.abhishek.recipefinder.network.RetrofitInstance
import com.abhishek.recipefinder.repository.LoginRepository
import com.abhishek.recipefinder.viewModel.LoginViewModel
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.abhishek.recipefinder.viewModel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    /*FirebaseAuth*/
    val firebaseAuth = FirebaseAuth.getInstance()

    // ✅ Provide repository instance
    val repository = RetrofitInstance.provideRecipeRepository()
    val searchRepository = RetrofitInstance.provideSearchRepository()
    val loginRepository = LoginRepository(firebaseAuth)

    // ✅ Create ViewModel using factory
    val recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(repository))
    val searchViewModel: SearchViewModel =
        viewModel(factory = SearchViewModelFactory(searchRepository))
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(loginRepository))

    NavHost(
        navController = navController,
        startDestination = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) "loginScreen" else "splashScreen"
    ) {
        composable("loginScreen") { LoginScreen(navController, loginViewModel) }
        composable("splashScreen") { SplashScreen(navController) }
        composable("homeScreen") { HomeScreen(navController, recipeViewModel, searchViewModel) }
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
