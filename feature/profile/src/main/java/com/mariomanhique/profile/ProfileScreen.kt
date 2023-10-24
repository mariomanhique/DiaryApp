package com.mariomanhique.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.util.fetchImageFromFirebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
   viewModel: ProfileViewModel = hiltViewModel()
){



    var userData = viewModel.getCurrentUser()
    val context = LocalContext.current
    val galleryState = viewModel.galleryState
    var username by remember {
        mutableStateOf(userData?.username.toString())
    }

    Box {

        ProfileContent(
            imageProfile =userData?.profilePictureUrl.toString(),
            username = username,
            onSelectImage = {imageUrl->
               val type = context.contentResolver.getType(imageUrl)?.split("/")?.last() ?: "jpg"
               viewModel.addImage(
                   image = imageUrl,
                   imageType = type
               )
            },
            onProfileSaved = {
                viewModel.uploadImageToFirebase()
                viewModel.updateProfile(
                    username = username,
                    imageUrl = galleryState.images.first().image.toString()
                )
            },
            onValueChanged = {
                username = it
            }
        )
    }
}

