package com.example.chatapplication.NavigationScreens

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapplication.Components.GoogleSignInButton
import java.nio.file.WatchEvent


@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSignInButtonPressed: (String, String) -> Unit,
    onSignUpButtonPressed: () -> Unit,
    onGoogleSignIn: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(40.dp)
    ) {
        Text(text = "Chat", fontSize = 25.sp, color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 50.dp, 0.dp, 0.dp),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {
                Text(text = "username")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "password")
            },
            label = {
                Text(text = "password")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedButton(
            onClick = { onSignInButtonPressed(email, password)},
            modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)) {
            Text(
                text = "Sign In",
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }

        GoogleSignInButton(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp),
            onSignIn = {googleIdToken, rawNonce -> onGoogleSignIn(googleIdToken, rawNonce)},
            onSignInFail = {fail -> }
        )

        Text(
            text = "Sign Up?",
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp)
                .clickable() {
                    onSignUpButtonPressed()
                },
            textAlign = TextAlign.Right,
            color = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    SignIn(
        email = "aaa@gmail.com",
        onEmailChange = {},
        password = "password",
        onPasswordChange = {},
        onSignInButtonPressed = {a, b ->},
        onSignUpButtonPressed = {},
        onGoogleSignIn = {a, b ->}
    )
}
