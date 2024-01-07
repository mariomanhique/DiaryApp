package com.example.diaryapp.presentation.screens.profile.navigation

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.diaryapp.presentation.components.DisplayAlertDialog
import com.example.diaryapp.presentation.screens.home.HomeViewModel
import com.example.diaryapp.presentation.screens.profile.ProfileScreen
import com.example.diaryapp.presentation.screens.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val profile_route = "profile_route"
fun NavController.navigateToProfile(navOptions: NavOptions?=null){
    this.navigate(profile_route, navOptions)
}
fun NavGraphBuilder.profileRoute(
    profileViewModel: ProfileViewModel,
    navigateToAuth: () -> Unit
){

    composable(route = profile_route){
        val context = LocalContext.current
        val viewModel: HomeViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        var signOutDialogState by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        ProfileScreen(
            profileViewModel = profileViewModel,
            onDeleteClicked = {
                deleteAllDialogOpened = it
                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
            },
            onLogoutClicked = {
                signOutDialogState = it
            },
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            dialogOpened = signOutDialogState,
            onCloseDialog = {
                signOutDialogState = false
            },
            onYesClicked = {
                signOutDialogState  = false
                scope.launch(Dispatchers.IO) {
                    FirebaseAuth.getInstance().signOut()
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
                                }
                            }
                        )
                    }
                }
            }
        )
    }

}