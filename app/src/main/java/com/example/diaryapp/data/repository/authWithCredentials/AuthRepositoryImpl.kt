package com.example.diaryapp.data.repository.authWithCredentials


import com.example.diaryapp.data.repository.authWithCredentials.utils.await
import com.example.diaryapp.presentation.screens.auth.UserData
import com.example.diaryapp.model.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
): AuthRepository {

    override suspend fun signIn(username: String, password: String): Result<Boolean> {
        val firebaseAuth = FirebaseAuth.getInstance()

        return try {
            firebaseAuth.signInWithEmailAndPassword(username,password).await().user
            Result.Success(true)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun signUp(email: String, username: String, password: String): Result<Boolean> {
        val  firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance().collection("profile")

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await().user
            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
            result?.uid?.let {userId->
                firestore.document(userId).set(
                    firestore.document(userId).set(
                        UserData(
                            userId,
                            username = username,
                            profilePictureUrl = ""
                        )
                    )
                )
            }
            Result.Success(true)
        }catch (e: Exception){
            Result.Error(e)
        }

    }

}