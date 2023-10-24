package com.mariomanhique.firestore.repository.profileRepository

import com.mariomanhique.util.model.RequestState
import com.mariomanhique.util.model.UserData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile()
    fun saveProfile(name:String, profile:String): RequestState<String>
    fun updateProfile(userData: UserData):RequestState<String>
}