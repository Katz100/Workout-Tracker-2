package com.example.chatapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.chatapplication.NavigationScreens.SignIn
import com.example.chatapplication.domain.model.NetworkResult
import org.junit.Rule
import org.junit.Test

class SignInTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signInTestAllElements() {
        composeTestRule.setContent {
            SignIn(
                email = "someEmail@gmail.com",
                password = "password",
                onGoogleSignIn = {a, b ->},
                onSignInButtonPressed = {a, b ->},
                onSignUpButtonPressed = {},
                onEmailChange = {},
                onPasswordChange = {},
                isError = null
            )
        }

        composeTestRule.onNodeWithText("Chat").assertIsDisplayed()
        composeTestRule.onNodeWithText("username").assertIsDisplayed()
        composeTestRule.onNodeWithText("password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up?").assertIsDisplayed()
    }

    @Test
    fun signInTestErrorMessage() {
        val someError = "Error"
        composeTestRule.setContent {
            SignIn(
                email = "someEmail@gmail.com",
                password = "password",
                onGoogleSignIn = {a, b ->},
                onSignInButtonPressed = {a, b ->},
                onSignUpButtonPressed = {},
                onEmailChange = {},
                onPasswordChange = {},
                isError = NetworkResult.Error(someError)
            )
        }

        composeTestRule.onNodeWithText(someError).assertIsDisplayed()
    }
}