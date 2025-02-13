package com.abhishek.recipefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.abhishek.recipefinder.factory.RecipeViewModelFactory
import com.abhishek.recipefinder.model.ApiResponse
import com.abhishek.recipefinder.model.Recipe
import com.abhishek.recipefinder.model.RecipeResponse
import com.abhishek.recipefinder.network.RetrofitInstance
import com.abhishek.recipefinder.ui.ui.RecipeItem
import com.abhishek.recipefinder.ui.ui.RecipeList
import com.abhishek.recipefinder.viewModel.RecipeViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup the Jetpack Splash API
        installSplashScreen()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("splashScreen") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        // ✅ Define `details/{recipeId}` route to accept a dynamic argument
        composable(
            "details/{recipeId}/{recipeName}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.IntType },
                navArgument("recipeName") { type = NavType.StringType },
                //    navArgument("recipeImage") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            val recipeName = backStackEntry.arguments?.getString("recipeName")
            // val recipeImage = backStackEntry.arguments?.getString("recipeImage")
            if (recipeId != null) {
                DetailScreen(navController, recipeId, recipeName)
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    var isSplashVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(10000) // Simulate loading time
        isSplashVisible = false
    }

    AnimatedVisibility(
        visible = isSplashVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SplashScreen()
    }

    if (!isSplashVisible) {
        HomeScreen(navController)
    }
}

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_ic), // Replace with your logo
                contentDescription = "Splash Logo",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val repository = RetrofitInstance.provideRecipeRepository()
    val viewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(repository))
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
                is ApiResponse.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())

                is ApiResponse.Success -> {
                    val recipes = (recipeState as ApiResponse.Success<RecipeResponse>).data.recipes
                    // Use RecipeList to render the list of recipes
                    RecipeList(recipes = recipes, onClick = { recipe ->
                        val recipeId = recipe.id // Pass recipeId to the details screen
                        val recipeName = recipe.name
                  /*      val recipeImage = recipe.image*/
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    recipeId: Int?,
    recipeName: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipeName ?: "Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            val recipeImage = "https://cdn.dummyjson.com/recipe-images/1.webp"
            // Show Recipe Image
            if (recipeImage.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(recipeImage),
                    contentDescription = "Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recipe Details (Name, Difficulty, Rating, etc.)
            if (recipeName != null) {
                Text(
                    text = recipeName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = "Cuisine: ${"recipe.cuisine"}", color = Color.Gray)
            Text(text = "Difficulty: ${"recipe.difficulty"}", color = Color.Gray)
            Text(text = "Cook Time: ${"recipe.cookTimeMinutes"} min", color = Color.Gray)
            Text(
                text = "⭐ ${"recipe.rating"} (${"recipe.reviewCount"} reviews)",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredients
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Instructions
            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

        }
    }
}
