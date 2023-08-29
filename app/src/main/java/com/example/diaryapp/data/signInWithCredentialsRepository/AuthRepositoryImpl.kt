package com.example.diaryapp.data.signInWithCredentialsRepository


import com.example.diaryapp.data.signInWithCredentialsRepository.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
      return try{

          auth.signInWithEmailAndPassword(email,password).await().user

      }catch (e:FirebaseAuthException){
          e.printStackTrace()
         null
      }

    }

    override suspend fun signUp(email: String, password: String,name: String): FirebaseUser? {
        return try {

            val result = auth.createUserWithEmailAndPassword(email,password).await().user


            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

            result



        }catch (e:FirebaseAuthException){
            e.printStackTrace()
            null
        }
    }

    override fun logout() {
        auth.signOut()
    }
}