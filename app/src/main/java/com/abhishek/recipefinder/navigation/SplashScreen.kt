package com.abhishek.recipefinder.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController,) {
    var isSplashVisible by remember { mutableStateOf(true) }

    // Dummy check for user logged in status
    var isUserLoggedIn by remember { mutableStateOf(false) }

    // Delay for splash screen duration
    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds splash time
        isSplashVisible = false
        val destination = if (isUserLoggedIn) "home" else "login"
        navController.navigate(destination) {
            popUpTo("splashScreen") { inclusive = true } // Remove splash from backstack
         }
    }

    AnimatedVisibility(
        visible = isSplashVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        SplashScreenContent()
    }
}

@Composable
fun SplashScreenContent() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_android), // Ensure this image exists
                contentDescription = "Splash Logo",
                modifier = Modifier.size(150.dp), // Adjust size as needed
                contentScale = ContentScale.Inside // Prevents cropping
            )
            Text(
                text = "Abhishek Bisht",
                color = Color.Red,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center) // Adjust position
            )
        }
    }
}