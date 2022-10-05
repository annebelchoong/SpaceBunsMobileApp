package com.example.spacebunsmobileapp.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : ViewModel() {
    //    val applicationContext: Context = application.applicationContext
    private var currentUser: FirebaseUser? = null


    // TODO(6): Get shared preferences
    private fun getPreferences(ctx: Context): SharedPreferences {
//        return ctx.getSharedPreferences("AUTH", Context.MODE_PRIVATE)   // return plain text sharedPref
        return EncryptedSharedPreferences.create(
            "AUTH",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // TODO(7): Auto login from shared preferences
    suspend fun loginFromPreferences(ctx: Context) {
// auto login in user from sharePreferences
        val p = getPreferences(ctx)
        val email = p.getString("email", null)
        val password = p.getString("password", null)

        if (email != null && password != null)
            login(ctx, email, password)
    }

    private fun login(
        ctx: Context,
        email: String,
        password: String,
        remember: Boolean = false
    ) {

    }
}