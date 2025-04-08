package com.example.chatapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.chatapplication.di.SupabaseModule

@HiltAndroidApp
class ChatApplication: Application()