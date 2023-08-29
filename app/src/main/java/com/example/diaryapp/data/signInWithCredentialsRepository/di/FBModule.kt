package com.example.diaryapp.data.signInWithCredentialsRepository.di

import com.example.diaryapp.data.signInWithCredentialsRepository.AuthRepository
import com.example.diaryapp.data.signInWithCredentialsRepository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FBModule {

//
//
//    @Singleton
//    @Provides
//    fun provideFirebaseDatabase(): DatabaseReference
//            = FirebaseDatabase.getInstance().reference

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth
            = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepositoryImpl(auth: AuthRepositoryImpl): AuthRepository {
        return auth
    }
}