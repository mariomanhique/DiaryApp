package com.example.diaryapp.data.repository.authWithCredentials


import com.example.diaryapp.data.repository.authWithCredentials.utils.await
import com.example.diaryapp.presentation.screens.auth.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): AuthRepository {

    val ref = firestore.collection("profile")
    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        val firebaseAuth = FirebaseAuth.getInstance()
      return try{

          firebaseAuth.signInWithEmailAndPassword(email,password).await().user

      }catch (e:FirebaseAuthException){
          e.printStackTrace()
         null
      }

    }

    override suspend fun signUp(email: String, password: String,name: String): FirebaseUser? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await().user
            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

            result?.uid?.let {userId->
                ref.document(userId).set(
                    UserData(
                        userId,
                        username = name,
                        profilePictureUrl = ""
                    )
                )
            }

            result

        }catch (e:FirebaseAuthException){
            e.printStackTrace()
            null
        }
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

}