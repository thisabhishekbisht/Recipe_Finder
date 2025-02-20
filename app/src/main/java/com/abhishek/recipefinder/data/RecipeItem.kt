package com.abhishek.recipefinder.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold


            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.name ?: "No description available",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
