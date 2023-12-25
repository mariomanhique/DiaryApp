package com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.SignInScreen


const val signInNavigationRoute = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions? = null){
    this.navigate(signInNavigationRoute,navOptions)
}
fun NavGraphBuilder.signInRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToHome:()->Unit,
    navigateToSignUp:()->Unit,
){
    composable(signInNavigationRoute){

        val context = LocalContext.current

        SignInScreen(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = navigateToHome,
            navigateToSignUp = navigateToSignUp,
        )
    }
}