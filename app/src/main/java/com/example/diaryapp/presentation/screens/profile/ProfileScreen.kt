package com.example.diaryapp.presentation.screens.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diaryapp.util.fetchImageFromFirebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit,
    profileViewModel: ProfileViewModel,
){



    val userData = profileViewModel.userData.collectAsStateWithLifecycle().value
    var username by remember {
        mutableStateOf(userData.username)
    }
    val downloadedImage = remember {
        mutableStateListOf<Uri>()
    }

    val context = LocalContext.current
    val galleryState = profileViewModel.galleryState

    LaunchedEffect(Unit){
        if ( downloadedImage.isEmpty()) {

        fetchImageFromFirebase(
            remoteImagePath = userData.profilePictureUrl.toString(),
            onImageDownload = {
                downloadedImage.add(it)
            },
            onImageDownloadFailed = {}
        )
    }
}


    Box {

        Scaffold(
            topBar = {
                ProfileTopBar()
            }
        ) {
            ProfileContent(
                imageProfile = galleryState.image.value.image,
                username = username,
                onSelectImage = {imageUrl->
                    val type = context.contentResolver.getType(imageUrl)?.split("/")?.last() ?: "jpg"
                    profileViewModel.addImage(
                        image = imageUrl,
                        imageType = type
                    )
                },
                onProfileSaved = {
                    profileViewModel.updateUsername(
                        username = username
                    )
                },
                onValueChanged = {
                    username = it
                },
                onDeleteClicked = onDeleteClicked,
                onLogoutClicked = onLogoutClicked,
                onImageUpdated = {
                    profileViewModel.updateImageProfile(
                        galleryState.image.value.remoteImagePath
                    )
                },
                paddingValues = it
            )
        }
    }
}

