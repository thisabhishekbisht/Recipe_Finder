package com.abhishek.recipefinder.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.abhishek.recipefinder.R
import com.abhishek.recipefinder.activity.ui.utils.CustomText
import com.abhishek.recipefinder.activity.ui.utils.LoadImage
import com.abhishek.recipefinder.viewModel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    recipeId: Int?,
    recipeName: String?,
    viewModel: RecipeViewModel
) {
    val recipe = viewModel.selectedRecipe

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Recipe Details") },
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
            val recipeImage = recipe?.image
            // Show Recipe Image
            if (recipeImage?.isNotEmpty() == true) {
                LoadImage(
                    imageUrl = recipeImage,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recipe Details (Name, Difficulty, Rating, etc.)
            if (recipe?.name != null) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            CustomText(
                "Cuisine: ${recipe?.cuisine}",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                style = TextStyle.Default
            )
            CustomText(
                "Difficulty: ${recipe?.difficulty}",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                style = TextStyle.Default
            )
            CustomText(
                "Cook Time: ${recipe?.cookTimeMinutes} min",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                style = TextStyle.Default
            )
            CustomText(
                "⭐ ${recipe?.rating} (${recipe?.reviewCount} reviews)",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle.Default
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Ingredients
            CustomText(
                "Ingredients:",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleMedium
            )
            /*     Text(
                     text = "Ingredients :",
                     style = MaterialTheme.typography.titleMedium,
                     fontWeight = FontWeight.Bold
                 )*/
            CustomText(
                "${recipe?.ingredients}",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                style = TextStyle.Default
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Instructions
            CustomText(
                "Instructions:",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleMedium
            )

            CustomText(
                "${recipe?.instructions}",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                style = TextStyle.Default
            )

        }
    }
}