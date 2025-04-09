package com.example.chatapplication


import com.example.chatapplication.data.dto.ConversationDto
import com.example.chatapplication.data.dto.MessageDto
import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ConversationRepository
import com.example.chatapplication.data.repository.MessageRepository
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.viewmodel.ConversationViewModel
import com.example.chatapplication.viewmodel.MessageViewModel
import com.example.chatapplication.viewmodel.ProfileViewModel

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

    private lateinit var conversationRepository: ConversationRepository
    private lateinit var conversationViewModel: ConversationViewModel

    private lateinit var messageRepository: MessageRepository
    private lateinit var messageViewModel: MessageViewModel



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        profileRepository = mockk()
        viewModel = ProfileViewModel(profileRepository)

        conversationRepository = mockk()
        conversationViewModel = ConversationViewModel(conversationRepository)

        messageRepository = mockk()
        messageViewModel = MessageViewModel(messageRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun add() {
        assertEquals(2, 1 + 1)
    }

    @Test
    fun `Get message`() = runTest {
        val messageDto = MessageDto(
            id = "mock-id",
            conversationId = "mock-con-id",
            senderId = "mock-send-id",
            body = "hi",
            createdAt = "4/9/2025",
            read = false
        )

        coEvery { messageRepository.getMessage(any()) } returns messageDto

        messageViewModel.loadMessage("fake-id")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = messageViewModel.message.value
        assertEquals(messageDto.body, result?.body)
    }

    @Test
    fun `Get conversation`() = runTest {
        val conversationDto = ConversationDto(
            id = "mock-id",
            conversationName = "con1",
            createdAt = "4/8/2025"
        )

        coEvery { conversationRepository.getConversation(any()) } returns conversationDto

        conversationViewModel.loadConversation("mock-id")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("con1", conversationViewModel.conversation.value?.conversationName)
    }
    @Test
    fun `Get user's display name`() = runTest {
        val profileDto = ProfileDto(
            id = "mock-id",
            displayName = "Cody Hopkins"
        )

        coEvery { profileRepository.getProfile(any()) } returns profileDto

        viewModel.loadProfile("mock-id")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Cody Hopkins", viewModel.profile.value?.displayName)
    }

    @Test
    fun `Change user's display name`() = runTest {
        // Create mock data
        val profileDto = ProfileDto(
            id = "mock-id",
            displayName = "Cody Hopkins"
        )

        // expected result
        val updatedProfileDto = profileDto.copy()

        // Mock repository behavior
        coEvery { profileRepository.getProfile(any()) } returns profileDto
        coEvery { profileRepository.updateDisplayName(any(), any()) } answers {
            // get second argument (Cody)
            val newName = arg<String>(1)
            // Return the updated profile with the new display name
            updatedProfileDto.copy(displayName = newName)
        }

        viewModel.loadProfile("mock-id")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateDisplayName("mock-id", "Cody")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert that the display name was updated correctly
        assertEquals("Cody", viewModel.profile.value?.displayName)
    }

}