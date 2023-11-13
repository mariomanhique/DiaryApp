package com.example.diaryapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.example.diaryapp.ui.DiaryAppState
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInRoute
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.navigateToSignUp
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpRoute
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.home.navigation.diariesRoute
import com.mariomanhique.profile.ProfileViewModel
import com.mariomanhique.profile.extractImagePath
import com.mariomanhique.profile.navigation.profileRoute
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.util.fetchImageFromFirebase
import writeRoute

@Composable
fun NavigationHost(
    startDestination:String,
    appState: DiaryAppState,
    onDataLoaded: () -> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit,
    diaries: Diaries
){
    val navController = appState.navController
    val profileViewModel: ProfileViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = startDestination
        ){

        diariesRoute(
            onDataLoaded = onDataLoaded,
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            },
            paddingValues = paddingValues,
            windowSizeClass = windowSizeClass,
            diaries = diaries
        )

        signInRoute(
            navigateToHome = {destination->
                 appState.navigateToTopLevelDestination(destination)
            },
            navigateToSignUp = {
                appState.navigateToSignUp()
            },
            onDataLoaded = onDataLoaded,
            destinations = appState.topLevelDestinations
            )

        signUpRoute(
            navigateToHome = {destination->
                 appState.navigateToTopLevelDestination(destination)
            },
            navigateToSignIn = {
                appState.navigateToSignIn()
            },
            onDataLoaded = onDataLoaded,
            destinations = appState.topLevelDestinations,
        )

        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            },
            paddingValues = paddingValues
        )
        profileRoute(
            profileViewModel =profileViewModel,
            onDeleteClicked = onDeleteClicked,
            onLogoutClicked = onLogoutClicked
        )
    }
}



