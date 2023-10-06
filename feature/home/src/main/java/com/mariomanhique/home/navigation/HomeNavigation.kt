package com.mariomanhique.home.navigation

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mariomanhique.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.mariomanhique.home.HomeScreen
import com.mariomanhique.home.HomeViewModel
import com.mariomanhique.ui.components.DisplayAlertDialog
import com.mariomanhique.util.Screen
import com.mariomanhique.util.model.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeRoute(
    navigateToAuth: () ->Unit,
    navigateToWrite: ()-> Unit,
    navigateToWriteWithArgs: (String)-> Unit,
    onDataLoaded: () ->Unit
){

    composable(route = Screen.Home.route){

        val context = LocalContext.current
        val viewModel: HomeViewModel = hiltViewModel()
        val auth: AuthWithCredentialsViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val diaries by viewModel.diaries.collectAsStateWithLifecycle()

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var dialogState by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        LaunchedEffect(key1 =diaries){
            if(diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onNavigateToWrite = navigateToWrite,
            onSignOutClicked = {
                dialogState = true
            },
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onDeleteDiariesClicked = {
                deleteAllDialogOpened = true
            },
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            dateIsSelected= viewModel.dateIsSelected,
            onDateSelected = {
                viewModel.getDiaries(it)
            },
            onDateReset = {
                viewModel.getDiaries()
            },

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
                                        Toast.makeText(context,"Diaries Deleted", Toast.LENGTH_SHORT).show()
                                        drawerState.close()
                                    }
                                }

                            },
                            onError = {error->

                                scope.launch {
                                    if (error.message == "No internet connection."){
                                        Toast.makeText(context,"You need internet connection " +
                                                "to perform this action", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context,"${error.message}", Toast.LENGTH_SHORT).show()
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