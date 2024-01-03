package com.example.diaryapp.presentation.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import com.example.diaryapp.model.GalleryImage
import com.example.diaryapp.model.GalleryState
import com.example.diaryapp.presentation.screens.auth.UserData
import com.example.diaryapp.util.fetchImageFromFirebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.diaryapp.data.repository.profileRepository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    val galleryState = GalleryState()

    private var _userData: MutableStateFlow<UserData> =
        MutableStateFlow(UserData("","",""))

    val userData = _userData.asStateFlow()

    init {
        getCurrentUser()
    }
    private fun getCurrentUser(){
        viewModelScope.launch {
            profileRepository.getProfile().collect{

               it?.let {
                   fetchImageFromFirebase(
                       remoteImagePath = it.profilePictureUrl.toString(),
                       onImageDownload = {downloadedImage->
                           galleryState.addImageProfile(
                               GalleryImage(
                                   image = downloadedImage,
                                   remoteImagePath = extractImagePath(
                                       fullImageUrl = downloadedImage.toString()
                                   )
                               )
                           )
                       },
                       onImageDownloadFailed = {}
                   )
                   _userData.value = it
               }
            }
        }
    }


    fun addImage(
        image: Uri,
        imageType:String,
    ){
        val user = FirebaseAuth.getInstance().currentUser
        val remoteImagePath = "images/${user?.uid}/" +
                "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        if (remoteImagePath.isNullOrEmpty()){
            //Nothing
        }else{
            galleryState.addImageProfile(
                GalleryImage(
                    image = image,
                    remoteImagePath = remoteImagePath
                )
            )
        }

    }

    private fun uploadImageToFirebase(){
        viewModelScope.launch {
          val  galleryImage = galleryState.image.value
            val storage = FirebaseStorage.getInstance().reference
            val imagePath = storage.child(galleryImage.remoteImagePath)

            imagePath.putFile(galleryImage.image).addOnProgressListener{
                 it.uploadSessionUri
            }
        }
    }
    fun updateUsername(username: String){
        if (username.isEmpty()){
            return
        }
        viewModelScope.launch {
            uploadImageToFirebase()
            profileRepository
                .updateUsername(username)
        }
    }

    fun updateImageProfile(
        imageUrl: String
    ){
        if (imageUrl.isEmpty()){
            return
        }
        viewModelScope.launch {
            uploadImageToFirebase()
            profileRepository.updateImageProfile(
                imageUrl
            )
        }
    }
}

fun extractImagePath(fullImageUrl: String): String {
    val chunks = fullImageUrl.split("%2F")
    val imageName = chunks[2].split("?").first()
    return "images/${Firebase.auth.currentUser?.uid}/$imageName"
}