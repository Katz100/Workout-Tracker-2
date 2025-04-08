package com.example.chatapplication.data.repository.impl

import android.app.Application
import com.example.chatapplication.data.dto.ProfileDto
import com.example.chatapplication.data.repository.ProfileRepository
import com.example.chatapplication.domain.model.Profile
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : ProfileRepository {

    override suspend fun getProfile(
        id: String
    ): ProfileDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("profile").select(columns = Columns.list("id", "display_name")) {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<ProfileDto>()
        }
    }

    override suspend fun updateDisplayName(
        id: String,
        newName: String
    ) {
        withContext(Dispatchers.IO) {
            postgrest.from("profile").update({
                set("display_name", newName)
            }) {
                filter {
                    eq("id", id)
                }
            }
        }
    }


}