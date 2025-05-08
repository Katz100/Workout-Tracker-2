package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
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
import com.example.chatapplication.NavigationScreens.HomePage
import com.example.chatapplication.NavigationScreens.SignIn
import com.example.chatapplication.NavigationScreens.SignUp
import com.example.chatapplication.ScreenB
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.viewmodel.SignInViewModel
import com.example.chatapplication.viewmodel.SignUpViewModel
import com.example.chatapplication.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.observeOn
import kotlinx.serialization.Serializable
import javax.inject.Inject

const val TAG = "MainApp"


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
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()
                NavHost(
                    navController = navController,
                    startDestination = SplashScreen
                ) {

                    // Check to see if the user's session is active
                    composable<SplashScreen> {
                        val session = userAuthViewModel.uiState.collectAsState().value.session
                        LaunchedEffect(session) {
                            Log.e("AUTHSESSION2", "session value: ${session.toString()}")
                            delay(1000)
                            when(session) {
                                is SessionStatus.NotAuthenticated -> {
                                    navController.navigate(SignInScreen) {
                                        popUpTo(SplashScreen) { inclusive = true }
                                    }
                                }
                                is SessionStatus.Authenticated -> {
                                    navController.navigate(ScreenB) {
                                        popUpTo(SplashScreen) { inclusive = true }
                                    }
                                }

                                SessionStatus.Initializing -> TODO()
                                is SessionStatus.RefreshFailure -> {
                                    navController.navigate(SignInScreen) {
                                        popUpTo(SplashScreen) { inclusive = true }
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    composable<SignInScreen> {
                        val signInViewModel: SignInViewModel = hiltViewModel()
                        val signInUiState = signInViewModel.uiState.collectAsState().value
                        val signInResponse = signInViewModel.response.collectAsState().value

                        var hasNavigated by remember { mutableStateOf(false) }

                         // When _response is now Successful, user has successfully logged in so navigate to next screen.
                        if (signInResponse is NetworkResult.Success && signInResponse.data == true && !hasNavigated) {
                            LaunchedEffect(Unit) {
                                hasNavigated = true
                                navController.navigate(ScreenB) {
                                    popUpTo(SignInScreen) { inclusive = true }
                                }
                            }

                        }

                        SignIn(
                            email = signInUiState.email,
                            password = signInUiState.password,
                            onEmailChange = { signInViewModel.onEmailChange(it) },
                            onPasswordChange = { signInViewModel.onPasswordChange(it) },
                            onSignUpButtonPressed = {
                                navController.navigate(SignUpScreen)
                            },
                            onSignInButtonPressed = { email, password ->
                                //remove captured parameters
                                signInViewModel.onSignIn()
                            },
                            onGoogleSignIn = { googleIdToken, rawNonce ->
                                signInViewModel.onGoogleSignIn(googleIdToken, rawNonce)
                            },
                            isError = signInResponse
                        )

                    }

                    composable<SignUpScreen> {
                        val signUpViewModel: SignUpViewModel = hiltViewModel()
                        val signUpUiState = signUpViewModel.uiState.collectAsState().value
                        val signUpResponse = signUpViewModel.response.collectAsState().value
                        SignUp(
                            displayName = signUpUiState.displayName,
                            email = signUpUiState.email,
                            password = signUpUiState.password,
                            confirmPassword = signUpUiState.confirmPassword,
                            onEmailChange = { signUpViewModel.onEmailChange(it) },
                            onPasswordChange = { signUpViewModel.onPasswordChange(it) },
                            onConfirmPasswordChange = { signUpViewModel.onConfirmPasswordChange(it) },
                            onDisplayNameChange = { signUpViewModel.onDisplayNameChange(it) },
                            onSignUpButtonPressed = { signUpViewModel.onSignUp() },
                            isError = signUpResponse
                        )
                    }

                    composable<ScreenB> {
                        HomePage(
                            onLogOut = {
                                userAuthViewModel.logOut()
                                navController.navigate(SignInScreen) {
                                    popUpTo(ScreenB) { inclusive = true }
                                }
                            }
                        )
                    }
                }
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
object ScreenB
