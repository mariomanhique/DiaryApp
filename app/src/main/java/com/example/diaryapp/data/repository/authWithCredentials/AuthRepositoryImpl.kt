package com.example.diaryapp.data.repository.authWithCredentials


import com.example.diaryapp.data.repository.authWithCredentials.utils.await
import com.example.diaryapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override suspend fun signIn(email: String, password: String): FirebaseUser? {
      return try{

          firebaseAuth.signInWithEmailAndPassword(email,password).await().user

      }catch (e:FirebaseAuthException){
          e.printStackTrace()
         null
      }

    }

    override suspend fun signUp(email: String, password: String,name: String): FirebaseUser? {
        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await().user
            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

            result

        }catch (e:FirebaseAuthException){
            e.printStackTrace()
            null
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}