package com.example.diaryapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.data.repository.firebaseDB.Diaries
import com.example.diaryapp.data.repository.firebaseDB.FirestoreRepository
import com.example.diaryapp.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :ViewModel() {

    val user = FirebaseAuth.getInstance().currentUser
    var _diaries: MutableStateFlow<Diaries> = MutableStateFlow(RequestState.Idle)
    val diaries = _diaries.asStateFlow()


    init{
      observeAllDiaries()
//        insertDiary(Diary(mood="Angry", title = "Hurreh", description = "Tired as fuck"))
    }
    private fun observeAllDiaries(){
        viewModelScope.launch {
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
        viewModelScope.launch {
            val result = firestoreRepository.deleteAllDiary()

            if (result is RequestState.Success){

                onSuccess(result.data)

            } else {
                onError(Exception(""))
            }
        }
    }




}