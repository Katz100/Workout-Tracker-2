package com.example.chatapplication.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination

class NavEvent(
    private val context: Context,
    private val onRouteChanged: (String?) -> Unit,
): NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val route = destination.route
        onRouteChanged(route)
        val intent = Intent(context, TimerService::class.java)
        context.stopService(intent)
    }
}