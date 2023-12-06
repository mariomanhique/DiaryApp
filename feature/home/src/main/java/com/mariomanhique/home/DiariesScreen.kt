package com.mariomanhique.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mariomanhique.util.model.RequestState
import com.mariomanhique.firestore.repository.firebaseDB.Diaries

@Composable
 fun HomeScreen(
    diaries: Diaries,
    navigateToWriteWithArgs: (String)-> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    viewModel: DiariesViewModel = hiltViewModel()

){

    val diariesList = viewModel.diaries.collectAsStateWithLifecycle().value


    var padding by remember { mutableStateOf(PaddingValues()) }

                padding = paddingValues
                when (diariesList) {
                is RequestState.Success -> {
                    Log.d("Home Content", "HomeScreen: Success")
                        when(windowSizeClass.widthSizeClass){
                            WindowWidthSizeClass.Compact -> {
                                HomeContentPortrait(
                                    paddingValues = paddingValues,
                                    diaryNotes = diariesList.data,
                                    onClick = navigateToWriteWithArgs
                                )
                            }
                            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                                HomeContentLandscape(
                                    paddingValues = paddingValues,
                                    diaryNotes = diariesList.data,
                                    onClick = navigateToWriteWithArgs
                                )
                            }
                    }
                }
                is RequestState.Error -> {
                    EmptyPage(
                        title = "Error",
                        subtitle = "${diariesList.error.message}"
                    )
                }
                is RequestState.Loading -> {
                    Log.d("Home Content", "HomeScreen: Loading")

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {}
    }
}
