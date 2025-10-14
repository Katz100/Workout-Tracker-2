package com.example.chatapplication.util

import android.util.Log

class WorkoutTrackingService {

    private var startTime: Long = 0
    private var totalTimeMillis: Long = 0

    fun startSession() {
        startTime = System.currentTimeMillis()
        Log.i("WorkoutTrackingService", "Started workout at: $startTime")
    }

    fun endSession() {
        Log.i("WorkoutTrackingService", "Ended workout at: $startTime")
        if (startTime != 0L) {
            totalTimeMillis += System.currentTimeMillis() - startTime
            startTime = 0
        }
        Log.i("WorkoutTrackingService", "Total time spent workout out: ${getTotalDurationMinutes()}")
    }

    fun getTotalDurationMinutes(): Long {
        return totalTimeMillis / 1000 / 60
    }

    fun reset() {
        startTime = 0
        totalTimeMillis = 0
    }
}
