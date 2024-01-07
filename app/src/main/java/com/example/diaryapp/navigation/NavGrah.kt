package com.example.diaryapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.example.diaryapp.presentation.screens.profile.ProfileViewModel
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation.signInRoute
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.navigation.navigateToSignUp
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.navigation.signUpRoute
import com.example.diaryapp.presentation.screens.home.navigation.homeRoute
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation.signInNavigationRoute
import com.example.diaryapp.presentation.screens.profile.navigation.profileRoute
import writeRoute

@Composable
fun NavigationHost(
    startDestination:String = signInNavigationRoute,
    appState: DiaryAppState,
    onDialogOpened: ()-> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    isNetworkAvailable: Boolean,
    paddingValues: PaddingValues,
    shouldShowLandscape: Boolean,
){
    val navController = appState.navController
    val profileViewModel: ProfileViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ){

        homeRoute(

            onDialogOpened = onDialogOpened,
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            },
            shouldShowLandscape = shouldShowLandscape
        )

        signInRoute(
            onShowSnackbar = onShowSnackbar,
            isNetworkAvailable = isNetworkAvailable,
            navigateToHome = {
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            },
            navigateToSignUp = navController::navigateToSignUp,
        )

        signUpRoute(
            onShowSnackbar = onShowSnackbar,
            isNetworkAvailable = isNetworkAvailable,
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
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(signInNavigationRoute)
            },
        )
    }
}