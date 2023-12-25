package com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials


const val signUpNavigationRoute = "sign_up_route"

fun NavController.navigateToSignUp(navOptions: NavOptions? = null){
    this.navigate(signUpNavigationRoute,navOptions)
}
fun NavGraphBuilder.signUpRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToHome:()->Unit,
    navigateToSignIn:()->Unit,
){
    composable(
        route = signUpNavigationRoute
    ){
        SignUpWithCredentials(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = navigateToHome,
            navigateToSignIn = navigateToSignIn,
        )
    }
}