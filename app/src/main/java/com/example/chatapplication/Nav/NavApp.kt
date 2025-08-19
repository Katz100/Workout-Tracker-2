package com.example.chatapplication.Nav

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.chatapplication.NavigationScreens.DevTestEnv
import com.example.chatapplication.NavigationScreens.HomePage
import com.example.chatapplication.NavigationScreens.SignIn
import com.example.chatapplication.NavigationScreens.SignUp
import com.example.chatapplication.TAG
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import com.example.chatapplication.viewmodel.DevEnvViewModel
import com.example.chatapplication.viewmodel.SignInViewModel
import com.example.chatapplication.viewmodel.SignUpViewModel
import com.example.chatapplication.viewmodel.UserAuthViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.Serializable


@Serializable
object SplashScreen

@Serializable
object SignUpScreen

@Serializable
object SignInScreen

@Serializable
object ScreenB

@Serializable
object DevPage

@Composable
fun NavApp(
    navController: NavHostController,
    userAuthViewModel: UserAuthViewModel,
    devEnvViewModel: DevEnvViewModel = hiltViewModel()
) {
    val isDev = true

    NavHost(
        navController = navController,
        startDestination = SplashScreen
    ) {
        composable<DevPage> {
            DevTestEnv(
                onLogOut = {
                    userAuthViewModel.logOut()
                    navController.navigate(SignInScreen) {
                    popUpTo(ScreenB) { inclusive = true }
                    }
                           },
                onAddExerciseClick = {
                    Log.i("DevApp", "Adding test exercise")
                    devEnvViewModel.insertExercise(
                        Exercise(
                            name = "test exercise",
                            description = "test desc",
                            sets = 3,
                            reps = listOf(8, 10, 12),
                            rest = 120,
                        )
                    )
                },
                onGetAllExercisesClicked = {
                    Log.i("DevApp", "Getting all exercises")
                    devEnvViewModel.getExercises()
                },
                onAddRoutineClicked = {
                    Log.i("DevApp", "Inserting test routine")
                    devEnvViewModel.insertRoutine(
                        Routine(
                           name = "Push day"
                        )
                    )
                },
                onGetAllRoutinesClicked = {
                    Log.i("DevApp", "Getting a routines")
                    devEnvViewModel.getRoutines()
                },
                onGetExercisesForRoutineClicked = {
                  Log.i("DevApp", "Getting exercises for routine")
                  devEnvViewModel.getUsersRoutineExercises("120cad98-89d7-4e93-81d0-5df9a088f22b")
                },
            )
        }

        // Check to see if the user's session is active
        composable<SplashScreen> {
            val session = userAuthViewModel.uiState.collectAsState().value.session
            LaunchedEffect(session) {
                Log.e(TAG, "session value: ${session.toString()}")
                when(session) {
                    is SessionStatus.NotAuthenticated -> {
                        Log.e(TAG, "Session not authenticated: ${session.toString()}")
                        navController.navigate(SignInScreen) {
                            popUpTo(SplashScreen) { inclusive = true }
                        }
                    }
                    is SessionStatus.Authenticated -> {
                        Log.e(TAG, "Session authenticated: ${session.toString()}")
                        if (isDev) {
                            Log.i("Nav", "Navigating to dev page")
                            navController.navigate(DevPage) {
                                popUpTo(SplashScreen) { inclusive = true }
                            }
                        } else {
                            Log.i("Nav", "Navigating to home page")
                            navController.navigate(ScreenB) {
                                popUpTo(SplashScreen) { inclusive = true }
                            }
                        }
                    }

                    is SessionStatus.Initializing -> {
                        Log.e(TAG, "Session initializing")
                    }
                    is SessionStatus.RefreshFailure -> {
                        Log.e(TAG, "Session authenticated: ${session.toString()}")
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

            var waitForConfirmation by remember { mutableStateOf(false) }

            if (signUpResponse is NetworkResult.Success) {
                waitForConfirmation = true
            }
            //TODO: navigate to separate screen so user can't go back to sign in page.
            if (waitForConfirmation) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Please confirm your email.")
                    CircularProgressIndicator()
                }

            } else {
                SignUp(
                    displayName = signUpUiState.displayName,
                    email = signUpUiState.email,
                    password = signUpUiState.password,
                    confirmPassword = signUpUiState.confirmPassword,
                    onEmailChange = { signUpViewModel.onEmailChange(it) },
                    onPasswordChange = { signUpViewModel.onPasswordChange(it) },
                    onConfirmPasswordChange = {
                        signUpViewModel.onConfirmPasswordChange(
                            it
                        )
                    },
                    onDisplayNameChange = { signUpViewModel.onDisplayNameChange(it) },
                    onSignUpButtonPressed = { signUpViewModel.onSignUp() },
                    isError = signUpResponse
                )
            }
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