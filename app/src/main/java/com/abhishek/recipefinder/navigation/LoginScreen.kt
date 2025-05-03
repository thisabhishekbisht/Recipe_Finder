package com.abhishek.recipefinder.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.recipefinder.R
import com.abhishek.recipefinder.activity.ui.utils.SocialLoginButton
import com.abhishek.recipefinder.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var googleSignInStarted by remember { mutableStateOf(false) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                loginViewModel.signInWithGoogle(idToken) { isSuccess ->
                    if (isSuccess) {
                        println("✅ Google login successful!")
                        save(context,account)
                        loginErrorMessage = null
                    } else {
                        println("❌ Google login failed!")
                        loginErrorMessage = "Google Sign-In failed. Please try again."
                    }
                }
            }
        } catch (e: ApiException) {
            println("❌ Google sign-in error: $e")
            loginErrorMessage = "Google Sign-In error: ${e.localizedMessage}"
        }
    }

    // Handling Google Sign-In intent
    LaunchedEffect(googleSignInStarted) {
        if (googleSignInStarted) {
            handleGoogleSignIn(context, googleSignInLauncher)
            googleSignInStarted = false // Reset state after handling sign-in
        }
    }

    // UI Layout with Scaffold for Snackbar support
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.login_background_img),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Persistent Bottom Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In for Your Favourite Recipes",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sign in to continue",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Buttons
                    SocialLoginButtons {
                        googleSignInStarted = true
                    }
                }
            }
        }

        // Trigger Snackbar when login fails
        loginErrorMessage?.let { message ->
            LaunchedEffect(message) {
                scope.launch { snackbarHostState.showSnackbar(message) }
                loginErrorMessage = null // Reset after showing message
            }
        }
    }
}

@Composable
fun SocialLoginButtons(onGoogleSignInClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SocialLoginButton(
            text = "Login with Google",
            icon = painterResource(id = R.drawable.ic_google),
            color = Color(0xFFDB4437),
            onClick = onGoogleSignInClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        SocialLoginButton(
            text = "Login with Facebook",
            icon = painterResource(id = R.drawable.ic_facebook),
            color = Color(0xFF1877F2),
            onClick = { println("Facebook login clicked!") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SocialLoginButton(
            text = "Login with X",
            icon = painterResource(id = R.drawable.ic_twitter),
            color = Color(0xFF1877F2),
            onClick = { println("Twitter login clicked!") }
        )
    }
}

// ✅ Now `handleGoogleSignIn()` simply launches the sign-in intent
fun handleGoogleSignIn(
    context: Context,
    googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {


    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("e7q4mTW0TaKhzdIM5qqehX:APA91bEzJpriyUg5jUPOjxTcZAtiaVEbD6verNZARh1NrwLoCWzmc_1WsPgqTqn7xFUU_P0DDGAj0c8d-Ct9kxZuwbgNHlpC4D-B9bLc_Sjwavj4ikeGPkg") // Replace with actual client ID
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    val signInIntent = googleSignInClient.signInIntent.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // ✅ Correctly applying flag
    }

// ✅ Properly launching the sign-in intent
    googleSignInLauncher.launch(signInIntent) // Ensure googleSignInLauncher is available
   }