package com.example.chatapplication.util

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chatapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TimerService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var duration = intent?.getIntExtra("duration", 120) ?: 120
        Timer.restTime.value = duration

        scope.launch {
            while (duration > 0) {
                delay(1.seconds)
                duration--
                Timer.restTime.value = duration
            }
            Timer.restTime.value = 0
            Timer.onTimerFinished()
            stopSelf()
        }

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d("Stopping", "Stopping Service")
        scope.cancel()
        Timer.restTime.value = 0
        return super.stopService(name)
    }

    override fun onDestroy() {
        scope.cancel()
        Timer.restTime.value = 0
        Toast.makeText(
            applicationContext, "Service execution completed",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("Stopped", "Service Stopped")
        super.onDestroy()
    }
}