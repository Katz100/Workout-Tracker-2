package com.example.chatapplication.data.repository.impl

import android.util.Log
import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.data.repository.RoutineRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import okio.IOException
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val client: SupabaseClient,
): RoutineRepository {

    companion object {
        const val ROUTINE_TABLE = "routine"
    }

    // TODO: update this value for when the user signs in with a new account
    val currentUserId = client.auth.currentUserOrNull()?.id
        ?: ""

    @OptIn(SupabaseExperimental::class)
     override val routineFlow: Flow<List<Routine>> = postgrest
        .from(ROUTINE_TABLE)
        .selectAsFlow(
            RoutineDto::id,
            filter = FilterOperation("userid", FilterOperator.EQ, currentUserId)
        )
        .map { list -> list.map { it.toDomain() } }
        .retryWhen { cause, attempt, ->
            cause is IOException
        }
        .catch { emptyList<Routine>() }

    override suspend fun createNewRoutine(routine: Routine): NetworkResult<List<Routine>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        val routineToInsert = routine.copy(userId = currentUserId)

        return try {
            val response = postgrest
                .from(ROUTINE_TABLE)
                .insert(routineToInsert.asDto()) {
                    select()
                }
                .decodeList<RoutineDto>()
                .map { it.toDomain() }
            NetworkResult.Success<List<Routine>>(response)
        } catch (e: Exception) {
            Log.e("ROUTINEREPO", "Error inserting routine $routine: ${e.message}")
            NetworkResult.Error("Error fecting routine: ${e.message}")
        }
    }

    override suspend fun getAllRoutines(): NetworkResult<List<Routine>> {
        val currentUserId = client.auth.currentUserOrNull()?.id
            ?: return NetworkResult.Error("User does not exist")

        return try {
            val response = postgrest
                .from(ROUTINE_TABLE)
                .select {
                    filter {
                        eq("userid", currentUserId)
                    }
                }.decodeList<RoutineDto>()
                .map { it.toDomain() }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e("ROUTINEREPO", "Error getting routine: ${e.message}")
            NetworkResult.Error("Error getting routines: ${e.message}")
        }
    }

    private fun Routine.asDto(): RoutineDto {
        return RoutineDto(
            id = this.id,
            userId = this.userId,
            name = this.name,
            description = this.description
        )
    }

    private fun RoutineDto.toDomain(): Routine {
        return Routine(
            id = this.id,
            userId = this.userId,
            name = this.name,
            description = this.description
        )
    }

}