package com.example.chatapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.Nav.NavApp
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.util.TimerService
import com.example.chatapplication.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import javax.inject.Inject

const val TAG = "MainApp"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. You can proceed with your foreground service logic.
         //   startTimerService()
        } else {
            // Permission denied. You should inform the user or handle the case gracefully.
            Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        createNotificationChannel(this)
        // handle deeplink from confirmation email
        supabaseClient.handleDeeplinks(intent) { session ->
            Log.d(TAG, "deep link: $session")
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContent {
            ChatApplicationTheme {
                val navController = rememberNavController()
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()

                NavApp(navController, userAuthViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, TimerService::class.java)
        stopService(intent)
    }

}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "timer",
            "Workout Timer",
            NotificationManager.IMPORTANCE_LOW // not DEFAULT
        ).apply { description = "Timer running in background" }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

}