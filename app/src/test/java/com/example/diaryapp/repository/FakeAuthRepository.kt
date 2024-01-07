package com.example.diaryapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import com.example.diaryapp.data.repository.authWithCredentials.Resource
import com.example.diaryapp.model.Result
import com.example.diaryapp.presentation.screens.auth.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class FakeAuthRepository: AuthRepository {

    private val user = mutableListOf<UserData>()
    private val fakeEmail = "me@gmail.com"
    private val fakePass = "1234"
    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return if (email == fakeEmail && password == fakePass){
            Result.Success(true)
        } else{
            Result.Error()
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<Boolean> {

        return Result.Success(user.add(UserData(username = name, profilePictureUrl = "")))

    }
}