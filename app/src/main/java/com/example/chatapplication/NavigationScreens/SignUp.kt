package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapplication.domain.model.NetworkResult

@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUpButtonPressed: () -> Unit,
    isError: NetworkResult<Boolean>?
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", fontSize = 25.sp, color = Color.Blue,
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

        OutlinedTextField(
            value = displayName,
            onValueChange = { onDisplayNameChange(it) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {
                Text(text = "display name")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {
                Text(text = "email")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {onConfirmPasswordChange(it)},
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "password")
            },
            label = {
                Text(text = "confirm password")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedButton(
            onClick = {onSignUpButtonPressed()},
            modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)) {
            Text(
                text = "Sign Up",
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }

    }
}