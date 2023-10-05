package com.example.diaryapp.presentation.screens.write

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariomanhique.util.model.Diary
import com.mariomanhique.util.model.Mood
import com.mariomanhique.util.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mariomanhique.database.entity.ImageToDelete
import com.mariomanhique.database.entity.ImageToUpload
import com.mariomanhique.firestore.repository.firebaseDB.FirestoreDB
import com.mariomanhique.firestore.repository.imageRepo.ImageRepository
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.ui.GalleryState
import com.mariomanhique.util.Constants.WRITE_SCREEN_ARG_KEY
import com.mariomanhique.util.fetchImagesFromFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor (
    private val savedStateHandle: SavedStateHandle,
    private val firestoreRepository: FirestoreDB,
    private val imageRepository: ImageRepository
): ViewModel() {

    val user = FirebaseAuth.getInstance().currentUser
    val galleryState = GalleryState()
    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        getSelectedDiary()
    }
    private fun getDiaryIdArgument(){
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARG_KEY
            )
        )
    }

     private suspend fun insertDiary(
         diary: Diary,
         onSuccess: () -> Unit,
         onError: (String) -> Unit
    ){
            if(user != null){
                val result = firestoreRepository.insertDiary(diary = diary.apply {
                    if (uiState.updatedDateTime != null){
                        date = Date.from(uiState?.updatedDateTime!!)
                    }
                })

                if (result is RequestState.Success){
                    uploadImagesToFirebase()
                    withContext(Dispatchers.Main){
                        onSuccess()
                    }
                }else if (result is RequestState.Error){
                    withContext(Dispatchers.Main){
                        onError(result.error.message.toString())
                    }
                }
            }
    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            if (user != null){
                if (uiState.selectedDiaryId !=null){
                    val result = firestoreRepository.deleteDiary(
                        uiState.selectedDiaryId.toString()
                    )
                    when(result){
                        is RequestState.Success -> {
                            deleteImagesFromFirebase(
                                uiState.selectedDiary?.imagesList
                            )
                            withContext(Dispatchers.Main){
                                onSuccess()
                            }

                        }
                        is RequestState.Error -> {
                            withContext(Dispatchers.Main){
                                onError(result.error.message.toString())
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {

            if (uiState.selectedDiaryId != null){
                Log.d("ID", "upsertDiary: ${uiState.selectedDiaryId}")
                updateDiary(
                    diary = diary,
                    onSuccess = onSuccess,
                    onError = onError
                )
            } else {
                insertDiary(
                    diary = diary,
                    onSuccess = onSuccess,
                    onError = onError
                )
          }

        }
    }


    private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        if (user != null){
            val result = firestoreRepository.updateDiary(diary = diary.apply {
                id = uiState.selectedDiaryId.toString()

                if(uiState.updatedDateTime != null){
                    uiState.updatedDateTime
                }
                date = uiState.selectedDiary?.date!!
            })
            uploadImagesToFirebase()
            galleryState.clearImagesToBeDeleted()
            if (result is RequestState.Success){
                withContext(Dispatchers.Main){
                    onSuccess()
                }
            } else if(result is RequestState.Error) {
                withContext(Dispatchers.Main){
                    onError(result.error.message.toString())
                }
            }

        }
    }

    private fun deleteImagesFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference
        if (images != null) {
            images.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch {
                            imageRepository.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }
                    }
            }
        } else {
            galleryState.imagesToBeDeleted.map {
                it.remoteImagePath
            }.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch{
                            imageRepository.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }
                    }
            }
        }
    }
    private fun uploadImagesToFirebase(){
        viewModelScope.launch {
            val storage = FirebaseStorage.getInstance().reference
            galleryState.images.forEach{galleryImage->
                val imagePath = storage.child(galleryImage.remoteImagePath)
                imagePath.putFile(galleryImage.image).addOnProgressListener{
                    val sessionUri = it.uploadSessionUri
                    if (sessionUri != null) {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageRepository.addImageToUpload(
                                ImageToUpload(
                                    remoteImagePath = galleryImage.remoteImagePath,
                                    imageUri = galleryImage.image.toString(),
                                    sessionUri = sessionUri.toString()
                                )
                            )
                        }
                    }
                }
            }
        }

    }
    private fun getSelectedDiary(){
        viewModelScope.launch {
         if(uiState.selectedDiaryId != null){
                firestoreRepository.getSelectedDiary(
                     diaryId = uiState.selectedDiaryId!!
                 ).catch {
                     emit(RequestState.Error(Exception("Diary is already deleted")))
                }.collect{diary->

                     if(diary is RequestState.Success){
                            diary.data?.let {selectedDiary->
                                setSelectedDiary(selectedDiary)
                                setTitle(title = selectedDiary.title)
                                setDescription(selectedDiary.description)
                                setMood(Mood.valueOf(selectedDiary.mood))
                                fetchImagesFromFirebase(
                                    remoteImagePaths = selectedDiary.imagesList,
                                    onImageDownload = {downloadedImage->
                                          galleryState.addImage(
                                              GalleryImage(
                                                  image  = downloadedImage,
                                                  remoteImagePath = extractImagePath(
                                                      fullImageUrl = downloadedImage.toString()
                                                  )
                                              )
                                          )
                                    },
                                    onImageDownloadFailed = {},
                                    onReadyToDisplay = {}
                                )
                            }


                     }
                 }


             }
         }
    }

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
    }
    fun setTitle(title: String){
        uiState = uiState.copy(
            title = title
        )
    }
    fun setDescription(description: String){
        uiState = uiState.copy(
            description = description
        )
    }
    fun setMood(mood: Mood){
        uiState = uiState.copy(
            mood = mood
        )
    }
    fun setSelectedDiary(diary: Diary){
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime){
        uiState = if (zonedDateTime != null){
            uiState.copy(
                updatedDateTime = zonedDateTime.toInstant()
            )
        } else {
            uiState.copy(
                updatedDateTime = null
            )
        }
    }

    fun addImage(
        image: Uri,
        imageType:String,
    ){
        val remoteImagePath = "images/${user?.uid}/" +
                "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        Log.d( "addImage", remoteImagePath)
       galleryState.addImage(
           GalleryImage(
               image = image,
               remoteImagePath = remoteImagePath
           )
       )
    }
}

data class UiState(
    val selectedDiaryId: String?=null,
    val selectedDiary: Diary?=null,
    val title: String = "",
    var description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: Instant? = null
)