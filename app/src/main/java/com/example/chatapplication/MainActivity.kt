package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.NavigationScreens.SignIn
import com.example.chatapplication.ScreenB
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.observeOn
import kotlinx.serialization.Serializable
import javax.inject.Inject

const val TAG = "MainApp"

/*
val supabase = createSupabaseClient(
    supabaseUrl = "https://jcqxxztakmhnpmcapfgr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjcXh4enRha21obnBtY2FwZmdyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM1Mzc5MjAsImV4cCI6MjA1OTExMzkyMH0.mW3CiZwPQi2ZbXJLmcn8l1VbdOXcqjy1G5r_NaDtTaE"
) {
   install(Auth)
    install(Postgrest)
}
*/
//client id: 305595485789-e14aebqncklp862np65f9srgv5390pg4.apps.googleusercontent.com

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChatApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenA
                ) {
                    composable<ScreenA> {
                        val signInViewModel: SignInViewModel = hiltViewModel()
                        val signInUiState = signInViewModel.uiState.collectAsState().value
                        val signInResponse = signInViewModel.response.collectAsState().value

                         // When _response is now Successful, user has successfully logged in so navigate to next screen.
                        if (signInResponse is NetworkResult.Success && signInResponse.data == true) {
                            navController.navigate(ScreenB) {
                                signInViewModel.resetResponse()
                            }
                        }
                        SignIn(
                            email = signInUiState.email,
                            password = signInUiState.password,
                            onEmailChange = { signInViewModel.onEmailChange(it) },
                            onPasswordChange = { signInViewModel.onPasswordChange(it) },
                            onSignUpButtonPressed = {},
                            onSignInButtonPressed = {a, b ->},
                            onGoogleSignIn = { googleIdToken, rawNonce ->
                                signInViewModel.onGoogleSignIn(googleIdToken, rawNonce)
                            }
                        )
                    }

                    composable<ScreenB> {
                        var email by remember { mutableStateOf("") }
                        LaunchedEffect(Dispatchers.IO) {
                            email =  supabaseClient.auth.retrieveUserForCurrentSession(updateSession = true).email.toString()
                        }

                        Text(text = email)
                    }
                }
            }
        }
    }
}

@Serializable
object ScreenA

@Serializable
object ScreenB
