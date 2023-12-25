package com.example.diaryapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.example.diaryapp.data.repository.firebaseDB.Diaries
import com.example.diaryapp.presentation.screens.profile.ProfileViewModel
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInRoute
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.navigateToSignUp
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpRoute
import com.example.diaryapp.presentation.screens.home.navigation.homeRoute
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInNavigationRoute
import com.example.diaryapp.presentation.screens.profile.navigation.profileRoute
import writeRoute

@Composable
fun NavigationHost(
    startDestination:String = signInNavigationRoute,
    appState: DiaryAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit,
){
    val navController = appState.navController
    val profileViewModel: ProfileViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ){

        homeRoute(
//            navigateToWrite = {
//                navController.navigate(Screen.Write.route)
//                Log.d("Route", "NavigationGraph: ${Screen.Write.route}")
//            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(signInNavigationRoute)
            },
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            }
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