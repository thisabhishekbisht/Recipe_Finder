package com.abhishek.recipefinder.activity.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import com.abhishek.recipefinder.viewModel.SearchViewModel

@Composable
fun CollapsibleToolbar(
    searchViewModel: SearchViewModel,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + slideInVertically(
            animationSpec = tween(durationMillis = 1000),
            initialOffsetY = { -it }
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + slideOutVertically(
            animationSpec = tween(durationMillis = 1000),
            targetOffsetY = { -it }
        )
    ) {
        RecipeSearchScreen(searchViewModel)
    }
}