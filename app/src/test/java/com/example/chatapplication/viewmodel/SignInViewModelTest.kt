package com.example.chatapplication.viewmodel

import android.net.Network
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

class SignInViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var signInViewModel: SignInViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        authenticationRepository = mockk()
        signInViewModel = SignInViewModel(authenticationRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onEmailChange() {
        val expected: String = "aaa@gmail.com"
        signInViewModel.onEmailChange("aaa@gmail.com")
        assertEquals(expected, signInViewModel.uiState.value.email)
    }

    @Test
    fun onPasswordChange() {
        val expected: String = "password"
        signInViewModel.onPasswordChange("password")
        assertEquals(expected, signInViewModel.uiState.value.password)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onSignUp() = runTest {
        val expected = NetworkResult.Success(true)

        coEvery { authenticationRepository.signUp(any(), any()) } returns expected

        signInViewModel.onEmailChange("someemail")
        signInViewModel.onPasswordChange("somepassword")
        signInViewModel.onSignUp()
        advanceUntilIdle()

        assertEquals(expected, signInViewModel.response.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onSignUpFailedCase() = runTest {
        val expected = NetworkResult.Error("network error", false)

        coEvery { authenticationRepository.signUp(any(), any()) } returns expected

        signInViewModel.onEmailChange("someemail")
        signInViewModel.onPasswordChange("somepassword")
        signInViewModel.onSignUp()
        advanceUntilIdle()

        assertEquals(expected, signInViewModel.response.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onSignIn() = runTest {
        val expected = NetworkResult.Success(true)

        coEvery { authenticationRepository.signIn(any(), any()) } returns expected

        signInViewModel.onEmailChange("someemail@gmail.com")
        signInViewModel.onPasswordChange("somepassword")

        signInViewModel.onSignIn()
        advanceUntilIdle()

        assertEquals(expected, signInViewModel.response.value)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGoogleSignIn() = runTest {
        val expected = NetworkResult.Success(true)

        coEvery { authenticationRepository.signInWithGoogle(any(), any()) } returns expected

        val someToken = "token"
        val someNonce = "wapdaw"

        signInViewModel.onGoogleSignIn(someToken, someNonce)
        advanceUntilIdle()

        assertEquals(expected, signInViewModel.response.value)
    }

    @Test
    fun resetResponse() {
        val expected = null

        signInViewModel.resetResponse()

        assertEquals(expected, signInViewModel.response.value)
    }

}