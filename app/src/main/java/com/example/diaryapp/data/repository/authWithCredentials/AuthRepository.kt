package com.example.diaryapp.data.repository.authWithCredentials
import com.example.diaryapp.model.Result


interface AuthRepository {
    suspend fun signIn(email:String,password:String): Result<Boolean>
    suspend fun signUp(email:String,password:String,name:String): Result<Boolean>

}