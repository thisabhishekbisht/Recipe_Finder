package com.abhishek.recipefinder.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.abhishek.recipefinder.R
import com.abhishek.recipefinder.navigation.NavGraph
import com.abhishek.recipefinder.ui.theme.MyAppTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        callFirebaseInstanceID()
        // Setup the Jetpack Splash API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
         //   installSplashScreen()
        }
        setContent {
            MyAppTheme {
                NavGraph()
            }
        }
    }

    private fun callFirebaseInstanceID() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.w("Token", token)
        })
    }
}

