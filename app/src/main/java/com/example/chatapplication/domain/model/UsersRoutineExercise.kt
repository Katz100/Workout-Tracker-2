package com.example.chatapplication.domain.model

data class UsersRoutineExercises(
    val exerciseName: String,
    val routineName: String,
    val sets: Int,
    val reps: List<Int>,
    val orderIndex: Int
)