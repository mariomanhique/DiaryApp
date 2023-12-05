package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.firestore.repository.firebaseDB.FirestoreDB
import com.mariomanhique.firestore.repository.firebaseDB.FirestoreRepository
import com.mariomanhique.util.model.Diary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.diaryapp.MainActivityUiState.Loading
import com.example.diaryapp.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firestoreRepo: FirestoreRepository
): ViewModel(){

    val uiState: StateFlow<MainActivityUiState> = firestoreRepo.getAllDiaries().map {
        Success(it)
    }.stateIn(
        viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val diaries: Diaries) : MainActivityUiState
}