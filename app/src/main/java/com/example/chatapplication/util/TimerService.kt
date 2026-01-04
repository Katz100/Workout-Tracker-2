package com.example.chatapplication.util

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.chatapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TimerService: Service() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var duration = intent?.getIntExtra("duration", 120) ?: 120
        Timer.restTime.value = duration

        val intent = Intent(applicationContext, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }
        val flag =
            PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            flag
        )

        val notification = NotificationCompat.Builder(applicationContext, "timer")
            .setContentTitle("Workout Timer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Starting timer")
            .addAction(0, "Stop timer", pendingIntent)
            .build()

        startForeground(1, notification)

        scope.launch {
            startRest(duration, applicationContext, pendingIntent)
            stopSelf()
        }

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d("TimerService","Stopping Service")
        scope.cancel()
        Timer.restTime.value = 0
        return super.stopService(name)
    }

    override fun onDestroy() {
        scope.cancel()
        Timer.restTime.value = 0
        Timer.endRest()
        Toast.makeText(
            applicationContext, "Service execution completed",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("TimerService","Service Stopped")
        super.onDestroy()
    }
}

suspend fun startRest(
    timeDuration: Int,
    applicationContext: Context,
    pendingIntent: PendingIntent,
) {
    var duration = timeDuration
    Timer.beginRest()
    val notificationManager = NotificationManagerCompat.from(applicationContext)
    while (duration > 0) {
        delay(1.seconds)
        duration--
        if (ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            val updatedNotification = NotificationCompat.Builder(applicationContext, "timer")
                .setContentTitle("Workout Timer")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("Time left: $duration sec")
                .addAction(0, "Stop timer", pendingIntent)
                .build()
            notificationManager.notify(1, updatedNotification)
        }
        Timer.restTime.value = duration
    }
    Timer.restTime.value = 0
    Timer.onTimerFinished()
}
