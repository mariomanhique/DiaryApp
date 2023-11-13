package com.mariomanhique.firestore.repository.profileRepository

import android.net.Uri
import com.mariomanhique.util.model.RequestState
import com.mariomanhique.util.model.UserData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile():Flow<UserData?>
    fun updateImageProfile(imageUrl: String):RequestState<String>
    fun updateUsername(username: String):RequestState<String>
}