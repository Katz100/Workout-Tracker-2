package com.example.chatapplication.data.repository.impl

import android.util.Log
import com.example.chatapplication.data.dto.WorkoutSessionDto
import com.example.chatapplication.data.repository.WorkoutSessionRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutSession
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class WorkoutSessionRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): WorkoutSessionRepository {
    companion object {
        const val WORKOUT_SESSION_TABLE = "workout_session"
        const val TAG = "WorkoutSessionRepository"
    }
    override suspend fun createNewWorkoutSession(workoutSession: WorkoutSession): NetworkResult<WorkoutSession> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        val sessionToInsert = workoutSession.copy(userId = currentUserId)

        return try {
            val response = postgrest
                .from(WORKOUT_SESSION_TABLE)
                .insert(sessionToInsert.asDto()) {
                    select()
                }
                .decodeSingle<WorkoutSessionDto>()
                .toDomain()
             NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    override suspend fun updateTotalWorkoutTimeForSession(
        workoutSessionId: String,
        time: Int
    ): NetworkResult<PostgrestResult> {
        return try {
            val response = postgrest
                .from(WORKOUT_SESSION_TABLE)
                .update(
                    {
                        set("totalworkouttime", time)
                    }
                ) {
                    filter {
                        eq("id", workoutSessionId)
                    }
                }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "There was an error updating the total workout time: ${e.message}")
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
            id = this.id,
            createdAt = this.createdAt,
            userId = this.userId,
            routineId = this.routineId,
            totalWorkoutTime = this.totalWorkoutTime
        )
    }
}