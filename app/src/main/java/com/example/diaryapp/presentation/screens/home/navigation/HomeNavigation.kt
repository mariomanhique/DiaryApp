package com.example.diaryapp.presentation.screens.home.navigation

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.diaryapp.presentation.components.DisplayAlertDialog
import com.example.diaryapp.presentation.screens.home.HomeScreen
import com.example.diaryapp.presentation.screens.home.HomeViewModel
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val homeRoute = "home_route"
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeRoute(
    onDialogOpened: ()-> Unit,
    navigateToWriteWithArgs: (String)-> Unit,
    shouldShowLandscape: Boolean
){

    composable(route = homeRoute){
        val viewModel: HomeViewModel = hiltViewModel()
        val diaries by viewModel.diaries.collectAsStateWithLifecycle()


        HomeScreen(
            diaries = diaries,

            onDialogOpened = onDialogOpened,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            dateIsSelected= viewModel.dateIsSelected,
            onDateSelected = {
                viewModel.getDiaries(it)
            },
            onDateReset = {
                viewModel.getDiaries()
            },
            shouldShowLandscape = shouldShowLandscape
            )


    }

}