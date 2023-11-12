package com.mariomanhique.firestore.repository.profileRepository

import com.mariomanhique.util.model.RequestState
import com.mariomanhique.util.model.UserData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile():Flow<UserData?>
    fun updateProfile(userData: UserData):RequestState<String>
}