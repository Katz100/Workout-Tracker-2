package com.example.chatapplication.data.repository.impl

import android.util.Log
import com.example.chatapplication.data.dto.WorkoutLogDto
import com.example.chatapplication.data.repository.WorkoutLogRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.WorkoutLog
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class WorkoutLogRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): WorkoutLogRepository {

    companion object {
        const val WORKOUT_LOG_TABLE = "workout_log"
        const val TAG = "WorkoutLogRepository"
    }

    override suspend fun logWeightForSession(log: WorkoutLog): NetworkResult<PostgrestResult> {
        return try {
            val logAsDto = log.asDto()
            val response = postgrest
                .from(WORKOUT_LOG_TABLE)
                .insert(logAsDto)
            Log.i(TAG, "Succesfully logged weight: ${logAsDto}")
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "There was an error logging a weight: ${e.message}")
            NetworkResult.Error(e.message)
        }
    }

    private fun WorkoutLog.asDto(): WorkoutLogDto {
        return WorkoutLogDto(
            id = this.id,
            sessionId = this.sessionId,
            exerciseId = this.exerciseId,
            set = this.set,
            weightUsed = this.weightUsed,
            createdAt = this.createdAt
        )
    }
}