package com.example.chatapplication.di

import com.example.chatapplication.data.repository.ConversationRepository
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.data.repository.impl.ConversationRepositoryImpl
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

}