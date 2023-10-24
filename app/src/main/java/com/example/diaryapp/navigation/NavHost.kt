package com.example.diaryapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.diaryapp.ui.DiaryAppState
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInNavigationRoute
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInRoute
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpNavigationRoute
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpRoute
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.home.navigation.diariesDestinationRoute
import com.mariomanhique.home.navigation.diariesRoute
import com.mariomanhique.profile.navigation.profileRoute
import com.mariomanhique.util.Screen
import writeRoute
import write_navigation_route

@Composable
fun NavigationHost(
    startDestination:String,
    appState: DiaryAppState,
    onDataLoaded: () -> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    diaries: Diaries
){


    val navController = appState.navController


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
            navigateToHome = {
                navController.navigate(diariesDestinationRoute)
            },
            navigateToSignUp = {
                navController.navigate(signUpNavigationRoute)
            },
            onDataLoaded = onDataLoaded
            )

        signUpRoute(
            navigateToHome = {
                navController.navigate(
                    diariesDestinationRoute
                )

            },
            navigateToSignIn = {
                navController.navigate(signInNavigationRoute)
            },
            onDataLoaded = onDataLoaded
        )

        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            },
            paddingValues = paddingValues
        )

        profileRoute()
    }
}



