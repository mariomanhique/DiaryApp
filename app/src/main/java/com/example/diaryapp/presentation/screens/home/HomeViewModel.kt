package com.example.diaryapp.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.connectivity.ConnectivityObserver
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import com.example.diaryapp.data.database.ImageToDeleteDao
import com.example.diaryapp.data.database.entity.ImageToDelete
import com.example.diaryapp.data.repository.firebaseDB.Diaries
import com.example.diaryapp.data.repository.firebaseDB.FirestoreRepository
import com.example.diaryapp.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.lang.Error
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val connectivity: NetworkConnectivityObserver,
    private val imageToDeleteDao: ImageToDeleteDao
) :ViewModel() {

    private lateinit var allDiariesJob: Job
    private lateinit var allFilteredDiariesJob: Job

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val user = FirebaseAuth.getInstance().currentUser
    private var _diaries: MutableStateFlow<Diaries> = MutableStateFlow(RequestState.Idle)
    val diaries = _diaries.asStateFlow()

    var dateIsSelected by mutableStateOf(false)
        private set// modify only withing viewModel


    init{
        getDiaries()
        viewModelScope.launch {
            connectivity.observe().collect{
                network = it
            }
        }
    }

    fun getDiaries(zonedDateTime: ZonedDateTime?=null){
        dateIsSelected = zonedDateTime != null
        _diaries.value = RequestState.Loading
        if(dateIsSelected && zonedDateTime != null){
            observeFilteredDiaries(zonedDateTime = zonedDateTime)
        } else{
            observeAllDiaries()
        }

    }

    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime){
       allFilteredDiariesJob = viewModelScope.launch {
           if(::allDiariesJob.isInitialized){
               allDiariesJob.cancelAndJoin()
           }
            firestoreRepository.getFilteredDiaries(zonedDateTime).collect{
                _diaries.value = it
            }
        }
    }
    private fun observeAllDiaries(){
        allDiariesJob = viewModelScope.launch {
            if(::allFilteredDiariesJob.isInitialized){
                allFilteredDiariesJob.cancelAndJoin()
            }
            if(user != null){
                firestoreRepository.getAllDiaries().distinctUntilChanged().collect{
                    _diaries.value = it
                }
            }

        }

    }

    fun deleteAllDiaries(
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ){

        if (network == ConnectivityObserver.Status.Available){
            val userId = user?.uid
            val imagesDirectory = "images/${userId}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach {ref->
                        val imagePath = "images/${userId}/${ref.name}"
                        storage.child(imagePath).delete()
                            .addOnFailureListener{
                                viewModelScope.launch(Dispatchers.IO){
                                    imageToDeleteDao.addImageToDelete(
                                        ImageToDelete(
                                            remoteImagePath = imagePath
                                        )
                                    )
                                }

                            }
                    }

                    viewModelScope.launch(Dispatchers.IO) {
                        val result = firestoreRepository.deleteAllDiary()

                        if (result is RequestState.Success){

                                onSuccess(result.data)

                        } else if(result is RequestState.Error) {
                            onError(Exception("Something went wrong"))
                        }
                    }

                }.addOnFailureListener{
                    onError(it)
                }
        }else{
            onError(Exception("No internet connection."))
        }

    }




}