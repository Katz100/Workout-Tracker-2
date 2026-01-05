package com.example.chatapplication.data.repository.impl

import android.util.Log
import com.example.chatapplication.data.dto.UsersRoutineExercisesDto
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.UsersRoutineExercises
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.Serializable
import javax.inject.Inject


@Serializable
data class GetUsersExercisesParams(
    val user_id: String,
    val routine_id: String
)

class UsersRoutineExercisesRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): UsersRoutineExercisesRepository {
    companion object {
        const val ROUTINE_EXERCISE_TABLE = "routine_exercise"
        const val TAG = "UsersRoutineExerciseRepo"
    }
    override suspend fun getUsersExercisesByRoutine(routineId: String): NetworkResult<List<UsersRoutineExercises>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")
        Log.i(TAG, "Getting exercises by routine for: $currentUserId, routineId: $routineId")

        return try {
            val response = postgrest
                .rpc(
                    function = "get_users_exercises_for_routine",
                    parameters = mapOf(
                        "user_id" to currentUserId,
                        "routine_id" to routineId,
                    )
                ).decodeList<UsersRoutineExercisesDto>()
                .map { it.toDomain() }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error("Unable to fetch user's exercises for the given routine: ${e.message}")
        }
    }

    override suspend fun updateExerciseWithNewOrder(
        exerciseId: String,
        newOrder: Int,
        routineId: String
    ): NetworkResult<PostgrestResult> {
        return try {
            val response = postgrest.from(ROUTINE_EXERCISE_TABLE)
                .update (
                    {
                        set("orderindex", newOrder)
                    }
                ) {
                    filter {
                        eq("exerciseid", exerciseId)
                        eq("routineid", routineId)
                    }
                }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    private fun UsersRoutineExercisesDto.toDomain(): UsersRoutineExercises {
        return UsersRoutineExercises(
            exerciseName = this.exerciseName,
            routineName = this.routineName,
            sets = this.sets,
            reps = this.reps,
            orderIndex = this.orderIndex,
            exerciseId = this.exerciseId,
            rest = this.rest,
        )
    }
}