package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
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

        return try {
            val response = postgrest
                .from(EXERCISE_TABLE)
                .insert(exerciseToInsert)
            NetworkResult.Success<PostgrestResult>(response)
        } catch (e: Exception) {
            NetworkResult.Error<PostgrestResult>("Error inserting exercise: ${e.message}")
        }

    }

    override suspend fun getAllExercises(): NetworkResult<List<Exercise>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        return try {
            val response = postgrest
                .from(EXERCISE_TABLE)
                .select(Columns.ALL) {
                    filter {
                        eq("userid", currentUserId)
                    }
                }.decodeList<ExerciseDto>()
                .map { it.toDomain() }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error("Error fetching user exercises: ${e.message}")
        }
    }

    override suspend fun getExerciseById(exerciseId: String): NetworkResult<List<Exercise>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        return try {
            val response = postgrest
                .from(EXERCISE_TABLE)
                .select(Columns.ALL) {
                    filter {
                        eq("userid", currentUserId)
                        eq("id", exerciseId)
                    }
                }.decodeList<ExerciseDto>()
                .map { it.toDomain() }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error("Error finding exercise")
        }
    }

    private fun ExerciseDto.toDomain(): Exercise {
        return Exercise(
            id = this.id,
            userId = this.userId,
            name = this.name,
            description = this.description,
            sets = this.sets,
            reps = this.reps,
            rest = this.rest,
        )
    }
}