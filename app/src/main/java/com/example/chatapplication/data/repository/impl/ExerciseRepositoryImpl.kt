package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.NetworkResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): ExerciseRepository{

    companion object {
        const val EXERCISE_TABLE = "exercise"
    }

    override suspend fun addExercise(exerciseDto: ExerciseDto) : NetworkResult<PostgrestResult> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        val exerciseToInsert = exerciseDto.copy(userId = currentUserId)

        val response = postgrest
            .from(EXERCISE_TABLE)
            .insert(exerciseToInsert)

        return NetworkResult.Success<PostgrestResult>(response)
    }
}