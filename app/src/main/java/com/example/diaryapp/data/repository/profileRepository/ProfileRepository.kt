package com.example.diaryapp.data.repository.profileRepository

import com.example.diaryapp.model.RequestState
import com.example.diaryapp.presentation.screens.auth.UserData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile():Flow<UserData?>
    fun updateImageProfile(imageUrl: String): RequestState<String>
    fun updateUsername(username: String):RequestState<String>
}