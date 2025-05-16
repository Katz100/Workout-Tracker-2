package com.example.chatapplication.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton
import io.github.jan.supabase.auth.auth

@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://jcqxxztakmhnpmcapfgr.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjcXh4enRha21obnBtY2FwZmdyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM1Mzc5MjAsImV4cCI6MjA1OTExMzkyMH0.mW3CiZwPQi2ZbXJLmcn8l1VbdOXcqjy1G5r_NaDtTaE"
        ) {
            install(Postgrest)
            install(Auth) {
                scheme = "com.supabase"
                host   = "login-callback"
            }

        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }


    /*
    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }
    */
}