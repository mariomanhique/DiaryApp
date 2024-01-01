package com.example.diaryapp.di

import android.content.Context
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideNetworkConnectivity(@ApplicationContext context: Context)=
        NetworkConnectivityObserver(context)
}

