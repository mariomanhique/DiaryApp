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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.util.fetchImageFromFirebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit,
   viewModel: ProfileViewModel = hiltViewModel()

){



    var userData = viewModel.userData.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val galleryState = viewModel.galleryState

    Box {

        ProfileContent(
            imageProfile =userData.profilePictureUrl.toString(),
            username = userData.username,
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
                    username = "",
                    imageUrl = galleryState.images.first().image.toString()
                )
            },
            onValueChanged = {

            },
            onDeleteClicked = onDeleteClicked,
            onLogoutClicked = onLogoutClicked
        )
    }
}

