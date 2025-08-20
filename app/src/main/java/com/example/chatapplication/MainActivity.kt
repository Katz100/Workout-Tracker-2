package com.example.chatapplication

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.Nav.NavApp
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.serialization.Serializable
import javax.inject.Inject

const val TAG = "MainApp"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // handle deeplink from confirmation email
        supabaseClient.handleDeeplinks(intent) { session ->
            Log.d(TAG, "deep link: $session")
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContent {
            ChatApplicationTheme {
                val navController = rememberNavController()
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()

                NavApp(navController, userAuthViewModel)
            }
        }
    }
}
