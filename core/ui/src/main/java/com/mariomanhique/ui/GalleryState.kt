package com.mariomanhique.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.net.toUri

//will allow to remember GalleryState across multiple recompositions
@Composable
fun rememberGalleryState(): GalleryState {
    return remember { GalleryState() }
}

class GalleryState {
    val images = mutableStateListOf<GalleryImage>()
    val image:MutableState<GalleryImage> = mutableStateOf(GalleryImage("".toUri(),""))
    val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImage(galleryImage: GalleryImage) {
        images.add(galleryImage)
    }

    fun removeImage(galleryImage: GalleryImage) {
        images.remove(galleryImage)
        imagesToBeDeleted.add(galleryImage)
    }

    fun remove(galleryImage: GalleryImage) {
        images.remove(galleryImage)
    }

    fun addImageProfile(galleryImage: GalleryImage) {
        image.value = galleryImage
    }


    fun clearImagesToBeDeleted(){
        imagesToBeDeleted.clear()
    }
}

class ImageProfileState {
    val images:MutableState<GalleryImage> = mutableStateOf(GalleryImage("".toUri(),""))
    val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImage(galleryImage: GalleryImage) {
        images.value = galleryImage
    }


    fun clearImagesToBeDeleted(){
        imagesToBeDeleted.clear()
    }
}

/**
 * A class that represents a single Image within a Gallery.
 * @param image The image URI inside a gallery.
 * @param remoteImagePath The path of the [image] where you plan to upload it.
 * */
data class GalleryImage(
    val image: Uri,
    val remoteImagePath: String = "",
)