package com.example.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.example.diaryapp.model.Diary
import com.example.diaryapp.model.GalleryImage
import com.example.diaryapp.model.GalleryState
import com.example.diaryapp.model.Mood
import com.example.diaryapp.presentation.components.ZoomableImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.time.ZonedDateTime
import com.example.diaryapp.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UiState,
    onDiarySaved: (Diary) -> Unit,
    galleryState: GalleryState,
    moodName: () -> String,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    pagerState: PagerState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () ->Unit,
    onBackPressed: () ->Unit,
    onImageSelect: (Uri) -> Unit,
    onImageDeleteClicked: (GalleryImage) -> Unit,
    paddingValues: PaddingValues


    ){
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var selectedGalleryImage by remember {
        mutableStateOf<GalleryImage?>(null)
    }
    //Updating the mood when selecting an existing diary
    LaunchedEffect(key1 =uiState.mood){
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated,
                onDeleteConfirmed = onDeleteConfirmed,
            ) {
                onBackPressed()
            }
        }
    ) {paddingValues->
        WriteContent(
            uiState = uiState,
            title = uiState.title,
            galleryState = galleryState,
            onTitleChanged = onTitleChanged,
            description = uiState.description,
            onDescriptionChanged = onDescriptionChanged,
            paddingValues = paddingValues,
            pagerState = pagerState,
            onDiarySaved = onDiarySaved,
            onImageSelect = onImageSelect,
            onImageClicked = {
                selectedGalleryImage = it
            }
        )
        
        AnimatedVisibility(visible = selectedGalleryImage != null) {
            Dialog(onDismissRequest = {
                selectedGalleryImage = null
            }) {
                selectedGalleryImage?.let {
                    ZoomableImage(
                        actionButton = R.string.deleteAction,
                        selectedGalleryImage = it,
                        onCloseClicked = {
                            selectedGalleryImage = null
                        },
                        onActionClicked = {
                            if(selectedGalleryImage != null){
                                onImageDeleteClicked(selectedGalleryImage!!)
                                selectedGalleryImage = null
                            }
                    } )

                }
            }
        }
    }
}


