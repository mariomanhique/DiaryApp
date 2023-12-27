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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val homeRoute = "home_route"
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeRoute(
    navigateToAuth: () ->Unit,
    navigateToWriteWithArgs: (String)-> Unit,
    shouldShowLandscape: Boolean
){

    composable(route = homeRoute){
        val context = LocalContext.current
        val viewModel: HomeViewModel = hiltViewModel()
        val auth: AuthWithCredentialsViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val diaries by viewModel.diaries.collectAsStateWithLifecycle()

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var dialogState by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        HomeScreen(
            diaries = diaries,

            onSettingsClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
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

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            dialogOpened = dialogState,
            onCloseDialog = {
                dialogState = false
            },
            onYesClicked = {
                dialogState      = false
                scope.launch(Dispatchers.IO) {
                    auth.signOut()
                    withContext(Dispatchers.Main){
                        navigateToAuth()
                    }
                }
            }
        )

        DisplayAlertDialog(
            title = "Delete All Diaries",
            message = "Are you sure you want to delete all diaries?",
            dialogOpened = deleteAllDialogOpened,
            onCloseDialog = {
                deleteAllDialogOpened = false
            },
            onYesClicked = {
                deleteAllDialogOpened = false
                scope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main){
                        viewModel.deleteAllDiaries(
                            onSuccess = {
                                if(it){
                                    scope.launch {
                                        Toast.makeText(context,"Diaries Deleted",Toast.LENGTH_SHORT).show()
                                        drawerState.close()
                                    }
                                }

                            },
                            onError = {error->

                                scope.launch {
                                    if (error.message == "No internet connection."){
                                        Toast.makeText(context,"You need internet connection " +
                                                "to perform this action",Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context,"${error.message}",Toast.LENGTH_SHORT).show()
                                    }
                                    drawerState.close()
                                }
                            }
                        )
                    }
                }
            }
        )
    }

}