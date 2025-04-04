package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

const val TAG = "MainApp"

val supabase = createSupabaseClient(
    supabaseUrl = "https://jcqxxztakmhnpmcapfgr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjcXh4enRha21obnBtY2FwZmdyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM1Mzc5MjAsImV4cCI6MjA1OTExMzkyMH0.mW3CiZwPQi2ZbXJLmcn8l1VbdOXcqjy1G5r_NaDtTaE"
) {
   install(Auth)
    install(Postgrest)
}

//client id: 305595485789-e14aebqncklp862np65f9srgv5390pg4.apps.googleusercontent.com
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var username by remember {mutableStateOf("John Doe")}
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GoogleSignInButton(
                            onSignIn = {username = it}
                        )
                        Text(text = username)
                    }

                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onSignIn: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val onClick: () -> Unit = {
        Log.e(TAG, "Clicked")
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it with sha-256
        // Providing a nonce is optional but recommended
        val rawNonce = UUID.randomUUID().toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in


        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("305595485789-e14aebqncklp862np65f9srgv5390pg4.apps.googleusercontent.com")
            .setNonce(hashedNonce) // Provide the nonce if you have one
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

             //   Log.e(TAG, googleIdToken)


                supabase.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                // Handle successful sign-in
                val user = supabase.auth.currentUserOrNull()
                val userEmail = user?.email ?: "No email"
                onSignIn(userEmail)
                Log.e(TAG, userEmail)

            } catch (e: GetCredentialException) {
                Log.e(TAG, e.toString())
                // Handle GetCredentialException thrown by `credentialManager.getCredential()`
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, e.toString())
                // Handle GoogleIdTokenParsingException thrown by `GoogleIdTokenCredential.createFrom()`
             } catch (e: RestException) {
                // Handle RestException thrown by Supabase
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                // Handle unknown exceptions
            }
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text("Sign in with Google")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatApplicationTheme {
        Greeting("Android")
    }
}