package com.karebet.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.karebet.smarthome.screen.GoogleLoginScreen
import com.karebet.smarthome.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoggedIn by remember {
                mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
            }

            if (!isLoggedIn) {
                GoogleLoginScreen(onLoggedIn = { isLoggedIn = true })
            } else {
                MainScreen(onLogout = { isLoggedIn = false })
            }
        }
    }
}



