package com.example.chatapplication.domain.model

data class Exercise(
    val id: String? = null,
    val userId: String? = null,
    val name: String,
    val description: String,
    val sets: Int,
    val reps: List<Int>
)