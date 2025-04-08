package com.example.chatapplication


import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.model.ProfileViewModel

import io.mockk.coEvery

import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var profileRepository: ProfileRepository
    private lateinit var viewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        profileRepository = mockk()
        viewModel = ProfileViewModel(profileRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun add() {
        assertEquals(3, 1 + 1)
    }

    @Test
    fun `Get user's display name`() = runTest {
        val profileDto = ProfileDto(
            id = "1776e865-ee99-4302-92ad-564acc64c9ca",
            displayName = "Cody Hopkins"
        )

        coEvery { profileRepository.getProfile(any()) } returns profileDto

        viewModel.loadProfile("1776e865-ee99-4302-92ad-564acc64c9ca")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Cody Hopkins", viewModel.profile.value?.displayName)
    }

}