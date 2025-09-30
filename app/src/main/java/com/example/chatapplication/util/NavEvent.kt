package com.example.chatapplication.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination

class NavEvent(private val context: Context): NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Log.i("NavEvent", "Navigation changed. controller: ${controller.context}, destination route: ${destination.route}")
        val intent = Intent(context, TimerService::class.java)
        context.stopService(intent)
    }

}