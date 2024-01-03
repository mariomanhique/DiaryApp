package com.example.diaryapp.data.repository.profileRepository

import com.example.diaryapp.model.RequestState
import com.example.diaryapp.presentation.screens.auth.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
):
        ProfileRepository {
    private val rf = firestore.collection("profile")
    private lateinit var updateUser: RequestState<String>

    override fun getProfile(): Flow<UserData?> {
        val user = FirebaseAuth.getInstance().currentUser
        return try {
          rf.document(user?.uid.toString())
                .snapshots()
                .map {
                    it.toObject<UserData>()
                }
        }catch (e:Exception){
            flow {
                UserData(
                    "","",""
                )
            }
        }
    }

    override fun updateImageProfile(imageUrl: String): RequestState<String> {
        val user = FirebaseAuth.getInstance().currentUser

        if (user !=null){
            return try {
                rf.document(user.uid)
                    .update(
                        mapOf(
                            "profilePictureUrl" to imageUrl
                        )
                    ).addOnSuccessListener {
                        updateUser = RequestState.Success("Sucesso")
                    }
                updateUser
            }catch (e:Exception){
                RequestState.Error(e)
            }
        }
        return RequestState.Error(Exception("user not authenticated"))
    }

    override fun updateUsername(username: String): RequestState<String> {
        val user = FirebaseAuth.getInstance().currentUser

        if (user !=null){
            return try {
                rf.document(user.uid)
                    .update(
                        mapOf(
                            "username" to username
                        )
                    ).addOnSuccessListener {
                        updateUser = RequestState.Success("Sucesso")
                    }
                updateUser
            }catch (e:Exception){
                RequestState.Error(e)
            }
        }
        return RequestState.Error(Exception("user not authenticated"))
    }

}