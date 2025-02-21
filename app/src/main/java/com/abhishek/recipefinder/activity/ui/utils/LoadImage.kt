package com.abhishek.recipefinder.activity.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abhishek.recipefinder.R
@Composable
fun LoadImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    placeholderRes: Int = R.drawable.placeholder, // Placeholder Image
    errorRes: Int = R.drawable.error_image // Error Image
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        // Load Image with Coil
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .placeholder(placeholderRes) // Shown while loading
                .error(errorRes) // Shown if loading fails
                .crossfade(true) // Smooth transition
                .build(),
            contentDescription = "Loaded Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            onSuccess = { isLoading = false },
            onError = { isLoading = false }
        )

        // Show Progress Indicator while loading
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
    }
}
