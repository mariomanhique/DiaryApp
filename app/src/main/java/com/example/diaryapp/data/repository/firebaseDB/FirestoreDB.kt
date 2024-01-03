package com.example.diaryapp.data.repository.firebaseDB

import android.util.Log
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import com.example.diaryapp.model.Diary
import com.example.diaryapp.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import javax.inject.Inject

class FirestoreDB @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository,
): FirestoreRepository {

    private lateinit var updatedDiary: RequestState<String>
    private val ref = firestore.collection("diary")

    override fun getDiaries(): Flow<Diaries>{
        val user = FirebaseAuth.getInstance().currentUser
       return if(user != null){
            try {
               ref.whereEqualTo(
                    "ownerId",
                    user.uid
                ).orderBy(
                    "date",
                    Query.Direction.DESCENDING
                ).snapshots().map {snapshot->
                    snapshot.toObjects<Diary>().run {
                        RequestState.Success(
                            data = groupBy {
                                it.date.toInstant()
                                     .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
                }
            }catch (e:Exception){
                flow {
                    emit(RequestState.Error(error = e))
                }
            }
        } else{
            flow {
                emit(RequestState.Error(error = UserNotAuthenticatedException()))
            }
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        val user = FirebaseAuth.getInstance().currentUser

        return callbackFlow {
            val query = ref
                .whereEqualTo("ownerId", user?.uid)
                .orderBy("date", Query.Direction.DESCENDING)

            val listener = query.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.d("Exception Handle", "getAllDiaries:$exception ")
                    // Handle the error
                    this.trySend(RequestState.Error(error = exception)).isSuccess
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    try {
                        val diaries = querySnapshot.toObjects<Diary>()
                        val groupedDiaries = diaries.groupBy {
                            it.date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        this.trySend(RequestState.Success(data = groupedDiaries)).isSuccess
                    } catch (e: Exception) {
                        this.trySend(RequestState.Error(error = e)).isSuccess
                        Log.d("Exception Handle", "getAllDiaries:$e ")
                    }
                }
            }

            // Cancel the listener when the Flow is closed
            awaitClose {
                listener.remove()
            }
        }.flowOn(Dispatchers.IO) // Specify the Dispatcher where this Flow will run
    }




    override fun insertDiary(diary: Diary): RequestState<Flow<Diary?>> {
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            try {
                val result = ref.document(diary.id)
                result.set(diary.apply {
                    ownerId = user.uid
                }).addOnFailureListener {
                    return@addOnFailureListener
                }
                return RequestState.Success(data = result.snapshots().map {
                    it.toObject<Diary>()
                })
            }catch (e:Exception){
                return RequestState.Error(error = e)
            }
        }else{
            return RequestState.Error(error = UserNotAuthenticatedException())
        }
    }

    override fun deleteDiary(diaryId: String): RequestState<String>? {
        val user = FirebaseAuth.getInstance().currentUser

        var deleted: RequestState<String>? = null
        return if(user != null){
            try {
                ref.document(diaryId)
                    .delete().addOnSuccessListener {
                        deleted = RequestState.Success("Success")
                    }.addOnFailureListener {
                        deleted = RequestState.Error(it)

                    }
                deleted

            }catch (e:Exception){
                RequestState.Error(error = e)
            }
        }else{
            RequestState.Error(error = UserNotAuthenticatedException())
        }
    }


    override suspend fun deleteAllDiary(): RequestState<Boolean>?{
        val user = FirebaseAuth.getInstance().currentUser

        var result: RequestState<Boolean>?=null

         if (user != null){
            try {
                val query = ref.whereEqualTo("ownerId", user.uid)
                query.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Handle the error here
                        result = RequestState.Error(error)
                        Log.d("Test: 1", "deleteAllDiary: ${error.message}")
                        return@addSnapshotListener
                    }

                    // Check if the snapshot is not null
                    if (snapshot != null) {
                        for (document in snapshot.documents) {
                            // Delete the document
                          document.reference.delete().addOnSuccessListener {
                              result = RequestState.Success(true)
                          }
                        }

                    }
                }

            }catch (e: Exception){
                Log.d("Test: 2", "deleteAllDiary: ${e.message}")
               result = RequestState.Error(e)
            }
        }else{
            result = RequestState.Error(UserNotAuthenticatedException())
             Log.d("Test: 2", "deleteAllDiary: Anything")

        }

       return result
    }

    override fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries> {
        val user = FirebaseAuth.getInstance().currentUser

        return callbackFlow {
            val query = ref
                .whereEqualTo("ownerId", user?.uid)
                .whereGreaterThan("date", Date.from(zonedDateTime.minusDays(1).toInstant()))
                .whereLessThan("date", Date.from(zonedDateTime.plusDays(1).toInstant()))
                .orderBy("date", Query.Direction.DESCENDING)

            val listener = query.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.d("Exception Handle", "getAllDiaries:$exception ")
                    // Handle the error
                    this.trySend(RequestState.Error(error = exception)).isSuccess
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    try {
                        val diaries = querySnapshot.toObjects<Diary>()
                        val groupedDiaries = diaries.groupBy {
                            it.date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        this.trySend(RequestState.Success(data = groupedDiaries)).isSuccess
                    } catch (e: Exception) {
                        this.trySend(RequestState.Error(error = e)).isSuccess
                        Log.d("Exception Handle", "getAllDiaries:$e ")
                    }
                }
            }

            // Cancel the listener when the Flow is closed
            awaitClose {
                listener.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateDiary(diary: Diary): RequestState<String>{
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            try {
                ref.document(diary.id)
                    .update(mapOf(
                        "title" to diary.title,
                        "description" to diary.description,
                        "mood" to diary.mood,
                        "date" to diary.date,
                        "imagesList" to diary.imagesList
                    )).addOnSuccessListener {
                        updatedDiary = RequestState.Success("Success")
                    }.addOnFailureListener{
                        updatedDiary = RequestState.Error(Exception("Failure"))
                    }
             return updatedDiary
            }catch (e:Exception){
                return RequestState.Error(error = e)
            }
        }else{
            return RequestState.Error(error = UserNotAuthenticatedException())
        }
    }

    override fun getSelectedDiary(diaryId: String): Flow<RequestState<Diary?>> {
        val user = FirebaseAuth.getInstance().currentUser

        return if (user != null){

            try {
               val diary = ref.whereEqualTo("id",diaryId)
                   .snapshots()
                   .map {
                       it.toObjects<Diary>()
                   }
                diary.map {mappedDiary->
                    RequestState.Success(data = mappedDiary.first())
                }
            }catch (e: Exception){
               flow { RequestState.Error(e) }
            }
        }else{
           flow{
               RequestState.Error(UserNotAuthenticatedException())
           }
        }
    }
}
private class UserNotAuthenticatedException: Exception("User not logged in")
