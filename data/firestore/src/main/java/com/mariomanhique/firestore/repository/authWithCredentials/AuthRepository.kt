package com.mariomanhique.firestore.repository.authWithCredentials

import com.google.firebase.auth.FirebaseUser
import io.realm.kotlin.mongodb.User

interface AuthRepository {

    val currentUser:FirebaseUser?

    val mongoCurrentUser: User?

    suspend fun signIn(email:String,password:String):FirebaseUser?
    suspend fun signUp(email:String,password:String,name:String):FirebaseUser?

    suspend fun mongoSignIn(email:String,password:String):User?

    suspend fun mongoSignUp(email:String,password:String)

    fun logout()
    suspend fun logoutFromMongo()
}