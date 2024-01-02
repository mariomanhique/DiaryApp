package com.mariomanhique.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.home.HomeScreen


const val home_destination_route = "home_route"
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(home_destination_route, navOptions)
}

fun NavGraphBuilder.diariesRoute(
    navigateToWriteWithArgs: (String)-> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
){
    composable(route = home_destination_route){
        HomeScreen(
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            paddingValues = paddingValues,
            windowSizeClass = windowSizeClass
            )
    }
}