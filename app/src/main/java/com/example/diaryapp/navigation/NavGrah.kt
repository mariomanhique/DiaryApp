package com.example.diaryapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mariomanhique.auth.navigation.signIn
import com.mariomanhique.auth.navigation.signUp
import com.mariomanhique.home.navigation.homeRoute
import com.mariomanhique.util.Screen
import writeRoute

@Composable
fun NavigationGraph(
    startDestination:String,
    navController: NavHostController,
    onDataLoaded: () -> Unit,


){

    NavHost(
        startDestination = startDestination,
        navController =navController,
        ){

        signIn(
            navigateToHome = {
                navController.navigate(
                    Screen.Home.route
                ) {
                    popUpTo(Screen.SignIn.route){
                        inclusive = true
                    }
                    launchSingleTop = true

                    restoreState = true

                    }
            },
            navigateToSignUp = {
                navController.navigate(Screen.SignUp.route)
            },
            onDataLoaded = onDataLoaded
            )
        signUp(
            navigateToHome = {
                navController.navigate(
                    Screen.Home.route
                ) {
                    popUpTo(Screen.SignUp.route){
                        inclusive = true
                    }
                    launchSingleTop = true

                    restoreState = true
                }

            },
            navigateToSignIn = {
                navController.navigate(Screen.SignIn.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = {
              navController.navigate(Screen.Write.route)
                Log.d("Route", "NavigationGraph: ${Screen.Write.route}")
            },
            navigateToAuth = {
                 navController.popBackStack()
                 navController.navigate(Screen.SignIn.route)
            },
            onDataLoaded = onDataLoaded,
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(it))
            }
        )
        writeRoute(
//            onDataLoaded= onDataLoaded,
            onBackPressed = {
                navController.popBackStack()
            }
        )

    }
}



