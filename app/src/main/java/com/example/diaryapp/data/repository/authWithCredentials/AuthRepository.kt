package com.example.diaryapp.data.repository.authWithCredentials

import com.google.firebase.auth.FirebaseUser
import io.realm.kotlin.mongodb.User

interface AuthRepository {

    val currentUser:FirebaseUser?


    suspend fun signIn(email:String,password:String):FirebaseUser?
    suspend fun signUp(email:String,password:String,name:String):FirebaseUser?


    fun logout()
}