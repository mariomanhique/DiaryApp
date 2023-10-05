package com.mariomanhique.auth.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.SignInScreen
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.SignUpWithCredentials
import com.mariomanhique.util.Screen


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
                Toast.makeText(context,"SignIn Failed, check", Toast.LENGTH_SHORT).show()
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