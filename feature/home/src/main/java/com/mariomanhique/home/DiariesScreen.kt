package com.mariomanhique.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mariomanhique.util.model.RequestState
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
 fun HomeScreen(
    diaries: Diaries,
    navigateToWriteWithArgs: (String)-> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass

){
    var padding by remember { mutableStateOf(PaddingValues()) }

                padding = paddingValues
                when (diaries) {
                is RequestState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        when(windowSizeClass.widthSizeClass){
                            WindowWidthSizeClass.Compact -> {
                                HomeContentPortrait(
                                    paddingValues = paddingValues,
                                    diaryNotes = diaries.data,
                                    onClick = navigateToWriteWithArgs
                                )
                            }
                            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                                HomeContentLandscape(
                                    paddingValues = paddingValues,
                                    diaryNotes = diaries.data,
                                    onClick = navigateToWriteWithArgs
                                )
                            }
                        }
                    }
                }
                is RequestState.Error -> {
                    EmptyPage(
                        title = "Error",
                        subtitle = "${diaries.error.message}"
                    )
                }
                is RequestState.Loading -> {
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
