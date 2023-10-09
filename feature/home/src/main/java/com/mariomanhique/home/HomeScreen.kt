package com.mariomanhique.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import com.mariomanhique.util.model.RequestState
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.ui.components.DiaryNavigation
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onSignOutClicked: ()-> Unit,
    onMenuClicked: ()-> Unit,
    onNavigateToWrite: ()-> Unit,
    onDeleteDiariesClicked: () -> Unit,
    navigateToWriteWithArgs: (String)-> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
){


    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavigationDrawer(
        drawerState = drawerState,
        onDiariesDelete = onDeleteDiariesClicked,
        onSignOutClicked = onSignOutClicked
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .statusBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                DiaryAppBar(
                    title = "My Diary",
                    navigationIcon = Icons.Default.Menu,
                    scrollBehavior = scrollBehavior,
                    dateIsSelected = dateIsSelected,
                    onDateSelected = onDateSelected,
                    onDateReset = onDateReset,
                    onMenuClicked = {
                        onMenuClicked()
                    })
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier= Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                    onClick = onNavigateToWrite
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add ,
                            contentDescription ="")

                    }
                }
            },
            bottomBar = {
              DiaryNavigation(
                  onHomeClicked = {},
                  onAccountClicked = {},
                  onDiariesClicked = {},
                  onGroupClicked = {}
                  )
            },

            ){

                padding = it
                when (diaries) {
                is RequestState.Success -> {
                    HomeContent(
                        paddingValues = it,
                        diaryNotes = diaries.data,
                        onClick = navigateToWriteWithArgs
                    )

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

    }
}