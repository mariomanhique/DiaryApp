package com.example.diaryapp.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryapp.model.Mood
import com.example.diaryapp.model.RequestState
import com.example.diaryapp.presentation.components.DisplayAlertDialog
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.SignInScreen
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials
import com.example.diaryapp.presentation.screens.home.HomeScreen
import com.example.diaryapp.presentation.screens.home.HomeViewModel
import com.example.diaryapp.presentation.screens.write.WriteScreen
import com.example.diaryapp.presentation.screens.write.WriteViewModel
import com.example.diaryapp.util.Constants
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

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


fun NavGraphBuilder.signIn(
    navigateToHome:()->Unit,
    navigateToSignUp:()->Unit,
    onDataLoaded: () ->Unit
){

    composable(
        route= Screen.SignIn.route
    ){

        val context = LocalContext.current
        LaunchedEffect(Unit){
            onDataLoaded()
        }

        SignInScreen(
            navigateToHome = navigateToHome,
            navigateToSignUp = navigateToSignUp,
            onSuccessSignIn = {

            },
            onFailedSignIn = {
                Toast.makeText(context,"SignIn Failed, check",Toast.LENGTH_SHORT).show()
            }

        )

    }
}

fun NavGraphBuilder.signUp(
    navigateToHome:()->Unit,
    navigateToSignIn:()->Unit,
    onDataLoaded: () ->Unit
){

    composable(
        route = Screen.SignUp.route
    ){

        LaunchedEffect(Unit){
                onDataLoaded()
        }
        SignUpWithCredentials(
            navigateToHome = navigateToHome,
            navigateToSignIn = navigateToSignIn
        )

    }
}

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

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
     onBackPressed: () -> Unit,
){

    composable(
        route= Screen.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARG_KEY){
            type = NavType.StringType
            nullable = true
        })
        ){





        val writeViewModel: WriteViewModel = hiltViewModel()
        val uiState = writeViewModel.uiState
        val context = LocalContext.current
        val pagerState = rememberPagerState()
        val galleryState = writeViewModel.galleryState
        val pageNumber by remember {
            derivedStateOf{
                pagerState.currentPage
            }
        }

        LaunchedEffect(
            key1 = uiState,
            block = {
                Log.d("Selected Diary", "writeRoute: ${uiState.selectedDiaryId} ")
            })

        WriteScreen(
            uiState = uiState,
            galleryState = galleryState,
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            onTitleChanged = {title->
                 writeViewModel.setTitle(title)
            },
            onDescriptionChanged = {description->
                writeViewModel.setDescription(description)
            },
            onDiarySaved = {diary->
               writeViewModel.upsertDiary(
                   diary = diary.apply {
                       mood = Mood.values()[pageNumber].name
                                       },
                   onSuccess = onBackPressed,
                   onError = {
                       onBackPressed()
//                       Toast.makeText(context,"Failed to update diary",Toast.LENGTH_SHORT).show()
                   }
               )
            },
            moodName = {Mood.values()[pageNumber].name},
            onDateTimeUpdated = {
                    writeViewModel.updateDateTime(zonedDateTime = it)
            },
            onDeleteConfirmed = {
                onBackPressed()
                writeViewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onImageSelect = {imageUri->
                val type = context.contentResolver.getType(imageUri)?.split("/")?.last() ?: "jpg"
                Log.d("addImagetype", "$imageUri")
                writeViewModel.addImage(
                    image = imageUri,
                    imageType = type
                )
            },
            onImageDeleteClicked = {
                galleryState.removeImage(it)
            }
        )
    }
}