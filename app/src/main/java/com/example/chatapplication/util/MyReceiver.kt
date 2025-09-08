package com.example.chatapplication.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyReceiver(
) : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onReceive(content: Context?, intent: Intent?) {
        Log.i("MyReceiver", "Received message from notification")
        val message = intent?.getStringExtra("MESSAGE")
        scope.launch {
            Timer.onStopTimer()
        }
    }
}