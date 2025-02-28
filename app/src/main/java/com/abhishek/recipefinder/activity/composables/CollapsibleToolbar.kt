package com.abhishek.recipefinder.activity.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun CollapsibleToolbar(
    isVisible: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        SearchBar(
            searchQuery = "",
            onSearchQueryChanged = onSearchQueryChanged,
            onSearch = onSearch
        )
    }
}
