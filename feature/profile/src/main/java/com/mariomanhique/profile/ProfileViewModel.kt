package com.mariomanhique.profile

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mariomanhique.firestore.repository.authWithCredentials.AuthRepository
import com.mariomanhique.firestore.repository.profileRepository.ProfileRepository
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.ui.GalleryState
import com.mariomanhique.util.fetchImageFromFirebase
import com.mariomanhique.util.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    val user = authRepository.currentUser
    val galleryState = GalleryState()

    init {
        getCurrentUser()
    }
    fun getCurrentUser() = authRepository.currentUser?.run {
        fetchImageFromFirebase(
                remoteImagePath = photoUrl.toString(),
                onImageDownload = {
                    Log.d("Image", "getCurrentUser: $it")

                    galleryState.addImage(
                        GalleryImage(
                            image = it,
                            remoteImagePath = extractImagePath(
                                fullImageUrl = it.toString()
                            )
                        )
                    )
                },
                onImageDownloadFailed = {},
                onReadyToDisplay = {}
            )
        Log.d("Image", "getCurrentUser: ${this.photoUrl}")
        UserData(
            userId = uid,
            username = displayName.toString(),
            profilePictureUrl = photoUrl.toString()
        )
    }

    fun addImage(
        image: Uri,
        imageType:String,
    ){
        val remoteImagePath = "images/${user?.uid}/" +
                "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        galleryState.addImage(
            GalleryImage(
                image = image,
                remoteImagePath = remoteImagePath
            )
        )
    }

    fun saveProfile(){

    }

    fun uploadImageToFirebase(){
        viewModelScope.launch {
          val  galleryImage = galleryState.images.first()
            val storage = FirebaseStorage.getInstance().reference
            val imagePath = storage.child(galleryImage.remoteImagePath)
            imagePath.putFile(galleryImage.image).addOnProgressListener{
                 it.uploadSessionUri
            }
        }
    }
    fun updateProfile(
        username:String,
        imageUrl: String
    ){
        viewModelScope.launch {

//            fetchImageFromFirebase(
//                remoteImagePath = imageUrl,
//                onImageDownload = {
//                    galleryState.addImage(
//                        GalleryImage(
//                            image = it,
//                            remoteImagePath = extractImagePath(
//                                fullImageUrl = it.toString()
//                            )
//                        )
//                    )
//                },
//                onImageDownloadFailed = {},
//                onReadyToDisplay = {}
//            )
            authRepository.currentUser?.updateProfile(
                userProfileChangeRequest {
                    this.displayName = username
                    this.photoUri = imageUrl.toUri()
                }
            )
        }
    }
}

fun extractImagePath(fullImageUrl: String): String {
    val chunks = fullImageUrl.split("%2F")
    val imageName = chunks[2].split("?").first()
    return "images/${Firebase.auth.currentUser?.uid}/$imageName"
}