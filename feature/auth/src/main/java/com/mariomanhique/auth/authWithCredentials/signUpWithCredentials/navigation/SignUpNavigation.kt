package com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.SignInScreen
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials
import com.mariomanhique.util.TopLevelDestination


const val signUpNavigationRoute = "sign_up_route"

fun NavController.navigateToSignUp(navOptions: NavOptions? = null){
    this.navigate(signUpNavigationRoute,navOptions)
}
fun NavGraphBuilder.signUpRoute(
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
            navigateToHome = navigateToHome,
            navigateToSignIn = navigateToSignIn,
            destinations = destinations
        )

    }
}