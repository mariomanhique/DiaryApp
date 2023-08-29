package com.example.diaryapp.data.signInWithCredentialsRepository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUser:FirebaseUser?

    suspend fun signIn(email:String,password:String):FirebaseUser?
    suspend fun signUp(email:String,password:String,name:String):FirebaseUser?
    fun logout()
}