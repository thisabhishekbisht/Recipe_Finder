package com.abhishek.recipefinder.utils

import android.content.Context
import android.net.Uri
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import androidx.core.net.toUri


class Utils {
    fun getUserInfo(context: Context): GoogleSignInAccount? {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val username = sharedPreferences.getString("username", null)
        val email = sharedPreferences.getString("email", null)
        val photoUrl = sharedPreferences.getString("photoUrl", null)

        return if (username != null && email != null) {
            GoogleSignInAccount.Builder()
                .setDisplayName(username)
                .setEmail(email)
                .setPhotoUrl(photoUrl?.toUri())
                .build()
        } else {
            null
        }
    }
}