package com.mercury.alcyone.DI

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.net.ConnectivityManager


@Module
@InstallIn(SingletonComponent::class)
object ConnectivityManager {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}