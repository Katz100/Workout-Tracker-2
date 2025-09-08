package com.example.chatapplication
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var duration = intent?.getIntExtra("duration", 120) ?: 120
        Timer.restTime.value = duration

        val notification = NotificationCompat.Builder(applicationContext, "timer")
            .setContentTitle("Workout Timer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Starting timer")
            .build()

        startForeground(1, notification)

        scope.launch {
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            while (duration > 0) {
                delay(1.seconds)
                duration--
                val updatedNotification = NotificationCompat.Builder(applicationContext, "timer")
                    .setContentTitle("Workout Timer")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Time left: $duration sec")
                    .build()

                notificationManager.notify(1, updatedNotification)
                Timer.restTime.value = duration
            }
            Timer.restTime.value = 0
            Timer.onTimerFinished()
            stopSelf()
        }

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d("Stopping","Stopping Service")
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
        Log.d("Stopped","Service Stopped")
        super.onDestroy()
    }
}