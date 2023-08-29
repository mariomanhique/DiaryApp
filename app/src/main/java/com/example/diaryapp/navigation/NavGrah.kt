package com.example.diaryapp.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.SignInScreen
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials
import com.example.diaryapp.presentation.screens.home.HomeScreen
import com.example.diaryapp.util.Constants

@Composable
fun NavigationGraph(startDestination:String, navController: NavHostController){
    NavHost(startDestination = startDestination,navController =navController){
        signIn(navController = navController)
        signUp(navController = navController)
        homeRoute(navController = navController)
        writeRoute()

    }
}


fun NavGraphBuilder.signIn(navController: NavController){

    composable(
        route= Screen.SignIn.route
    ){

        SignInScreen(navController = navController)

    }
}

fun NavGraphBuilder.signUp(navController: NavController){

    composable(
        route = Screen.SignUp.route
    ){

        SignUpWithCredentials(navController = navController)

    }
}

fun NavGraphBuilder.homeRoute(navController: NavController){

    composable(route = Screen.Home.route){
        HomeScreen(
            navController = navController
            )
    }

}

fun NavGraphBuilder.writeRoute(){
    composable(
        route= Screen.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARG_ID){
            type = NavType.StringType
            nullable = true
        })
        ){

    }
}