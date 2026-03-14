package com.example.chatapplication.util

import android.content.Context
import android.media.MediaPlayer
import com.example.chatapplication.R

class MyMediaPlayer (
    val context: Context,
) {
    var mediaPlayer: MediaPlayer? = null

    fun playSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.beep2)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    fun releaseMedia() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}