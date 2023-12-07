package com.example.diaryapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.example.diaryapp.ui.DiaryAppState
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInRoute
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.navigateToSignUp
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpRoute
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.home.navigation.diariesRoute
import com.mariomanhique.profile.ProfileViewModel
import com.mariomanhique.profile.navigation.profileRoute
import com.mariomanhique.util.TopLevelDestination
import writeRoute

@Composable
fun NavigationHost(
    startDestination:String,
    appState: DiaryAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
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
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            },
            paddingValues = paddingValues,
            windowSizeClass = windowSizeClass,
            diaries = diaries
        )

        signInRoute(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = {
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            },
            navigateToSignUp = navController::navigateToSignUp,
            )

        signUpRoute(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = {
                 appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            },
            navigateToSignIn = navController::navigateToSignIn,
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



