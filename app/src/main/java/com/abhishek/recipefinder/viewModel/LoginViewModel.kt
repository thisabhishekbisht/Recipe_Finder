package com.abhishek.recipefinder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.recipefinder.repository.LoginRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun signInWithGoogle(idToken: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            loginRepository.signInWithGoogle(idToken) { isSuccess ->
                onComplete(isSuccess)
            }
        }
    }

    // Add Facebook sign-in method here
    fun signInWithFacebook(credential: AuthCredential, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            loginRepository.signInWithFacebook(credential) { isSuccess ->
                onComplete(isSuccess)
            }
        }
    }
}