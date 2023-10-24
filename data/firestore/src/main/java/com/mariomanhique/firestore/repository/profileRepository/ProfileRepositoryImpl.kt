package com.mariomanhique.firestore.repository.profileRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.mariomanhique.util.model.RequestState
import com.mariomanhique.util.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

    class ProfileRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore): ProfileRepository {
    val rf = firestore.collection("profile")
    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var updateUser: RequestState<String>


    override fun getProfile() {

    }

    override fun saveProfile(name: String, profile: String): RequestState<String> {
       if (user!=null){
            try {
                val result = rf.document(user.uid)

                result.set(
                    hashMapOf(
                    "name" to name,
                    "profile" to profile,
                )
                ).addOnFailureListener{
                    return@addOnFailureListener
                }
                return RequestState.Success(data = "Success")
            } catch (e:Exception){
                return RequestState.Error(e)
            }
        } else{
            return RequestState.Error(Exception("There is no user to be saved"))
       }
    }


    override fun updateProfile(user: UserData): RequestState<String> {
        if(user!=null){

            try {
              rf.document(user.userId)
                    .update(
                        mapOf(
                           "username" to user.username,
                            "profilePictureUrl" to user.profilePictureUrl
                        )
                    ).addOnSuccessListener {
                        updateUser = RequestState.Success("Sucesso")
                    }

                return updateUser
            }catch (e:Exception){
                return RequestState.Error(e)

            }
        }else{
            return RequestState.Error(Exception(""))
        }
    }
}