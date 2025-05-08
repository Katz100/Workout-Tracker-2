package com.example.chatapplication.viewmodel

import com.example.chatapplication.data.repository.AuthenticationRepository
import com.example.chatapplication.domain.model.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.math.sign

class SignUpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var signUpViewModel: SignUpViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        authenticationRepository = mockk()
        signUpViewModel = SignUpViewModel(authenticationRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onEmailChange() {
        val expected = "testemail"
        signUpViewModel.onEmailChange(expected)
        assertEquals(expected, signUpViewModel.uiState.value.email)
    }

    @Test
    fun onPasswordChange() {
        val expected = "password"
        signUpViewModel.onPasswordChange(expected)
        assertEquals(expected, signUpViewModel.uiState.value.password)
    }

    @Test
    fun onConfirmPasswordChange() {
        val expected = "password"
        signUpViewModel.onConfirmPasswordChange(expected)
        assertEquals(expected, signUpViewModel.uiState.value.confirmPassword)
    }

    @Test
    fun onDisplayNameChange() {
        val expected = "name"
        signUpViewModel.onDisplayNameChange(expected)
        assertEquals(expected, signUpViewModel.uiState.value.displayName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onSignUp() = runTest {
        val expected = NetworkResult.Success(true)

        coEvery { authenticationRepository.signUp(any(), any()) } returns expected
        signUpViewModel.onEmailChange("email")
        signUpViewModel.onPasswordChange("password")
        signUpViewModel.onSignUp()
        advanceUntilIdle()

        assertEquals(expected, signUpViewModel.response.value)
    }

    @Test
    fun passwordsMatch() {
        signUpViewModel.onPasswordChange("password")
        signUpViewModel.onConfirmPasswordChange("password")
        assertEquals(true, signUpViewModel.passwordsMatch())
    }

}