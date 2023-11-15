package com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials
import com.mariomanhique.util.TopLevelDestination


const val signUpNavigationRoute = "sign_up_route"

fun NavController.navigateToSignUp(navOptions: NavOptions? = null){
    this.navigate(signUpNavigationRoute,navOptions)
}
fun NavGraphBuilder.signUpRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToHome:(TopLevelDestination)->Unit,
    destinations: List<TopLevelDestination>,
    navigateToSignIn:()->Unit,
    onDataLoaded: () ->Unit
){

    composable(
        route = signUpNavigationRoute
    ){

        LaunchedEffect(Unit){
            onDataLoaded()
        }
        SignUpWithCredentials(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = navigateToHome,
            navigateToSignIn = navigateToSignIn,
            destinations = destinations
        )

    }
}