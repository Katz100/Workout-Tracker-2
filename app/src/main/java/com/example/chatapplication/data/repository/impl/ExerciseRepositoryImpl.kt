package com.example.chatapplication.data.repository.impl

import android.util.Log
import com.example.chatapplication.data.dto.ExerciseDto
import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.data.dto.RoutineExerciseDto
import com.example.chatapplication.data.repository.ExerciseRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.RoutineExercise
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import okio.IOException
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): ExerciseRepository{

    companion object {
        const val EXERCISE_TABLE = "exercise"
        const val ROUTINE_EXERCISE_TABLE = "routine_exercise"
    }

    // TODO: update this value for when the user signs in with a new account
    val currentUserId = client.auth.currentUserOrNull()?.id
        ?: ""

    @OptIn(SupabaseExperimental::class)
    override val exerciseFlow: Flow<List<Exercise>> = postgrest
        .from(EXERCISE_TABLE)
        .selectAsFlow(
            ExerciseDto::id,
            filter = FilterOperation("userid", FilterOperator.EQ, currentUserId)
        )
        .map { list -> list.map { it.toDomain() } }
        .retryWhen { cause, attempt, ->
            cause is IOException
        }
        .catch { emptyList<Exercise>() }

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

    override suspend fun addExerciseToRoutine(
        routineId: String,
        exerciseId: String,
        orderIndex: Int
    ): NetworkResult<PostgrestResult> {
        val exerciseToInsert = RoutineExerciseDto(
            routineId = routineId,
            exerciseId = exerciseId,
            orderIndex = orderIndex,
        )
        return try {
            val response = postgrest
                .from(ROUTINE_EXERCISE_TABLE)
                .insert(exerciseToInsert)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e("EXERCISEREPO", "Error adding exercise to routine: ${e.message}")
            NetworkResult.Error("Error adding exercise to routine: ${e.message}")
        }
    }

    override suspend fun deleteExercise(exercise: Exercise): NetworkResult<PostgrestResult> {
        val exerciseId = exercise.id ?: return NetworkResult.Error("Exercise not found")

        return try {
            val response = postgrest
                .from(EXERCISE_TABLE)
                .delete {
                    filter { eq("id", exerciseId) }
                }
            Log.i("EXERCISEREPO", "Deleted exercise: $response")
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e("EXERCISEREPO", "Error deleting exercise: {$e.message}")
            NetworkResult.Error("Error deleting exercise")
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