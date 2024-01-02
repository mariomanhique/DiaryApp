package com.mariomanhique.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.mariomanhique.database.entity.ImageToDelete
import com.mariomanhique.database.entity.ImageToUpload
import java.time.Instant


/**
 * Download Images from firebase asynchronously
 * This function return imageUrl after each successful download
 */
fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            if (remoteImagePath.trim().isNotEmpty()) {
                Log.d("from db", "fetchImagesFromFirebase: $remoteImagePath")
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        Log.d("DownloadURL2", "$it")
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
