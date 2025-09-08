package com.example.chatapplication.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chatapplication.R
import com.example.chatapplication.util.MyReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkoutModule {
    @Singleton
    @Provides
    fun provideNotificationBuilder2(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }
        val flag = PendingIntent.FLAG_IMMUTABLE

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            flag
        )
        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle("Rest Timer")
            .setContentText("Time left: ")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .addAction(0, "Stop timer", pendingIntent)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Main Channel ID",
                "Main Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}