package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapplication.Components.CustomButton
import com.example.chatapplication.Components.CustomTextField
import com.example.chatapplication.Components.GoogleSignInButton
import com.example.chatapplication.domain.model.NetworkResult

@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSignInButtonPressed: (String, String) -> Unit,
    onSignUpButtonPressed: () -> Unit,
    onGoogleSignIn: (String, String) -> Unit,
    isError: NetworkResult<Boolean>?
) {
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome", fontSize = 25.sp, color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 50.dp, 0.dp, 0.dp),
            textAlign = TextAlign.Center
        )
        if (isError is NetworkResult.Error) {
            Text(
                text = isError.message.toString(),
                color = Color.Red,
                modifier = Modifier.padding(20.dp)
            )
        }
        CustomTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            placeholder = "Email",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "person") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .contentType(androidx.compose.ui.autofill.ContentType.EmailAddress),
        )

        CustomTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            placeholder = "Password",
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = "password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .contentType(androidx.compose.ui.autofill.ContentType.Password)
                .focusRequester(passwordFocusRequester),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignInButtonPressed(email, password)
                }
            )
        )

        CustomButton(
            onClick = { onSignInButtonPressed(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp),
            text = "Sign In",
            textModifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )

        GoogleSignInButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp),
            onSignIn = { googleIdToken, rawNonce -> onGoogleSignIn(googleIdToken, rawNonce) },
            onSignInFail = { fail -> }
        )

        Text(
            text = "Sign Up?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable() {
                    onSignUpButtonPressed()
                },
            textAlign = TextAlign.Right,
            color = Color.White
        )

        if (isError is NetworkResult.Loading) {
            CircularProgressIndicator()
        }
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
        onSignInButtonPressed = { a, b -> },
        onSignUpButtonPressed = {},
        onGoogleSignIn = { a, b -> },
        isError = NetworkResult.Success(true)
    )
}
