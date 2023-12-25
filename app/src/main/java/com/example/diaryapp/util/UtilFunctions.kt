package com.example.diaryapp.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.diaryapp.data.database.entity.ImageToDelete
import com.example.diaryapp.data.database.entity.ImageToUpload
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            if (remoteImagePath.trim().isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        Log.d("DownloadURL", "$it")
                        onImageDownload(it)
                        if (remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index) {
                            onReadyToDisplay()
                        }
                    }.addOnFailureListener {
                        onImageDownloadFailed(it)
                    }
            }
        }
    }
}

fun fetchImageFromFirebase(
    remoteImagePath: String,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed:(Exception) -> Unit,
    onReadyToDisplay: () -> Unit
){
    if(remoteImagePath.trim().isNotEmpty()){
        FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
            .addOnSuccessListener{
                onImageDownload(it)
            }.addOnFailureListener{
                onImageDownloadFailed(it)
            }
    }
}

fun retryUploadingImageToFirebase(
    imageToUpload: ImageToUpload,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath).putFile(
        imageToUpload.imageUri.toUri(),
        StorageMetadata(),
        imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}

fun retryDeletingImageFromFirebase(
    imageToDelete: ImageToDelete,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete()
        .addOnSuccessListener { onSuccess() }
}
