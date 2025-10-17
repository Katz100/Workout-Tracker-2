package com.example.chatapplication.data.repository.impl

import com.example.chatapplication.data.dto.WorkoutSessionDto
import com.example.chatapplication.data.repository.WorkoutSessionRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutSession
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class WorkoutSessionRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): WorkoutSessionRepository {
    companion object {
        const val WORKOUT_SESSION_TABLE = "workout_session"
    }
    override suspend fun createNewWorkoutSession(workoutSession: WorkoutSession): NetworkResult<List<WorkoutSession>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        val sessionToInsert = workoutSession.copy(userId = currentUserId)

        return try {
            val response = postgrest
                .from(WORKOUT_SESSION_TABLE)
                .insert(sessionToInsert.asDto()) {
                    select()
                }
                .decodeList<WorkoutSessionDto>()
                .map { it.toDomain() }
             NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    private fun WorkoutSession.asDto(): WorkoutSessionDto {
        return WorkoutSessionDto(
            userId = this.userId,
            routineId = this.routineId,
            totalWorkoutTime = this.totalWorkoutTime
        )
    }

    private fun WorkoutSessionDto.toDomain(): WorkoutSession {
        return WorkoutSession(
            userId = this.userId,
            routineId = this.routineId,
            totalWorkoutTime = this.totalWorkoutTime
        )
    }
}