package com.mercury.alcyone.DI

import com.example.alcyone.BuildConfig
import com.mercury.alcyone.Data.DataSources.TestDataSource3842
import com.mercury.alcyone.Data.Repos.Repository
import com.mercury.alcyone.Data.Repos.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Supabase {


    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.URL,
            supabaseKey = BuildConfig.API_KEY,
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSubRepository(
        test3842: TestDataSource3842
    ): Repository {
        return RepositoryImpl(test3842)
    }

    @Provides
    @Singleton
    fun provideSupabase(): Supabase {
        return Supabase
    }
}