package com.abhishek.recipefinder.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.recipefinder.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController,
    onSignInSuccess: () -> Unit
) {
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result: ActivityResult ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if(task.isSuccessful){
                    Log.d("LoginScreen", "Google sign-in success")
                    onSignInSuccess()                                        
                }else{
                    Log.d("LoginScreen", "Google sign-in failed")
                }
               
            } else {
                Log.d("LoginScreen", "Google sign-in failed")
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login Screen",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    Log.d("LoginScreen", "Sign-in button clicked")
                    val signInIntent = googleSignInClient.signInIntent
                    signInLauncher.launch(signInIntent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Image(painter = painterResource(id = R.drawable.icon_android), contentDescription ="Google Logo")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Sign in with Google")
            }
        }
    }
}
