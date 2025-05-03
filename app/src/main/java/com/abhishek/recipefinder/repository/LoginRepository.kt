package com.abhishek.recipefinder.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential

class LoginRepository(
    private val firebaseAuth: FirebaseAuth
) {
    fun signInWithGoogle(idToken: String, onComplete: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Add Facebook sign-in logic here
    fun signInWithFacebook(credential: AuthCredential, onComplete: (Boolean) -> Unit) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}