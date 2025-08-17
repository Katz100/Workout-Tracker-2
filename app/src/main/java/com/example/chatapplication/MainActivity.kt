package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.Components.CustomDrawer
import com.example.chatapplication.Nav.NavApp
import com.example.chatapplication.NavigationScreens.HomePage
import com.example.chatapplication.NavigationScreens.SignIn
import com.example.chatapplication.NavigationScreens.SignUp
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.viewmodel.SignInViewModel
import com.example.chatapplication.viewmodel.SignUpViewModel
import com.example.chatapplication.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.observeOn
import kotlinx.serialization.Serializable
import javax.inject.Inject

const val TAG = "MainApp"
// kj


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // handle deeplink from confirmation email
        supabaseClient.handleDeeplinks(intent) { session ->
            Log.e(TAG, "deep link: ${session.toString()}")
        }

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

@Serializable
object SplashScreen

@Serializable
object SignUpScreen

@Serializable
object SignInScreen

@Serializable
object HomePageScreen
