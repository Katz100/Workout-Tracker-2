package com.example.chatapplication.di

import com.example.chatapplication.data.repository.AuthenticationRepository
import com.example.chatapplication.data.repository.ConversationRepository
import com.example.chatapplication.data.repository.MessageRepository
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.data.repository.impl.AuthenticationRepositoryImpl
import com.example.chatapplication.data.repository.impl.ConversationRepositoryImpl
import com.example.chatapplication.data.repository.impl.MessageRepositoryImpl
import com.example.chatapplication.data.repository.impl.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent



@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl) : ConversationRepository

    @Binds
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl) : MessageRepository

    @Binds
    abstract fun bindAuthenticationRepository(impl: AuthenticationRepositoryImpl) : AuthenticationRepository

}