package com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.SignInScreen
import com.mariomanhique.util.TopLevelDestination


const val signInNavigationRoute = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions? = null){
    this.navigate(signInNavigationRoute,navOptions)
}
fun NavGraphBuilder.signInRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToHome:(TopLevelDestination)->Unit,
    destinations: List<TopLevelDestination>,
    navigateToSignUp:()->Unit,
    onDataLoaded: () ->Unit
){
    composable(signInNavigationRoute){

        val context = LocalContext.current
        LaunchedEffect(Unit){
            onDataLoaded()
        }

        SignInScreen(
            onShowSnackbar = onShowSnackbar,
            navigateToHome = navigateToHome,
            destinations = destinations,
            navigateToSignUp = navigateToSignUp,
            onSuccessSignIn = {

            },
            onFailedSignIn = {
                Toast.makeText(context,"SignIn Failed, check", Toast.LENGTH_SHORT).show()
            }

        )
    }
}