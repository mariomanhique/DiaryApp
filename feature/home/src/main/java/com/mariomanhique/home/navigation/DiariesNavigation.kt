package com.mariomanhique.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.firestore.repository.firebaseDB.Diaries
import com.mariomanhique.home.HomeScreen
import com.mariomanhique.util.model.RequestState


const val diariesDestinationRoute = "diaries_route"
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(diariesDestinationRoute, navOptions)
}

fun NavGraphBuilder.diariesRoute(
    navigateToWriteWithArgs: (String)-> Unit,
    onDataLoaded: () ->Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    diaries: Diaries
){
    composable(route = diariesDestinationRoute){
        LaunchedEffect(key1 =diaries){
            if(diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }
        HomeScreen(
            diaries = diaries,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            paddingValues = paddingValues,
            windowSizeClass = windowSizeClass
            )
    }
}