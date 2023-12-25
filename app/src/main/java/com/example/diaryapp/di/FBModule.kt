package com.example.diaryapp.di

import android.content.Context
import androidx.room.Room
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepositoryImpl
import com.example.diaryapp.data.repository.firebaseDB.FirestoreDB
import com.example.diaryapp.data.repository.firebaseDB.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.diaryapp.data.database.ImagesDatabase
import com.example.diaryapp.data.repository.profileRepository.ProfileRepository
import com.example.diaryapp.data.repository.profileRepository.ProfileRepositoryImpl
import com.example.diaryapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FBModule {


    @Singleton
    @Provides
    fun provideMongoAuth(): App.Companion
    = App.Companion

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth
            = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseUser(): FirebaseUser?
            = Firebase.auth.currentUser

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore
            = Firebase.firestore

    @Singleton
    @Provides
    fun provideFBRepository(firestore: FirestoreDB):FirestoreRepository
    = firestore

    @Singleton
    @Provides
    fun provideAuthRepositoryImpl(auth: AuthRepositoryImpl): AuthRepository {
        return auth
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ImagesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
             name = Constants.IMAGES_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideImageToUploadDao(imageDatabase: ImagesDatabase)
        = imageDatabase.imageToUploadDao()

    @Singleton
    @Provides
    fun provideImageToDeleteDao(imageDatabase: ImagesDatabase)
            = imageDatabase.imageToDeleteDao()

    @Singleton
    @Provides
    fun provideProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository {
        return profileRepository
    }
}