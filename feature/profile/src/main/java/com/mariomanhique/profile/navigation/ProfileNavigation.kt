package com.mariomanhique.profile.navigation

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.profile.ProfileScreen
import com.mariomanhique.profile.ProfileViewModel
import com.mariomanhique.ui.components.DisplayAlertDialog
import com.mariomanhique.util.Screen


const val profile_route = "profile_route"
fun NavController.navigateToProfile(navOptions: NavOptions?=null){
    this.navigate(profile_route, navOptions)
}
fun NavGraphBuilder.profileRoute(
){

    composable(route = profile_route){
        LaunchedEffect(Unit){

        }
        ProfileScreen()
    }

}