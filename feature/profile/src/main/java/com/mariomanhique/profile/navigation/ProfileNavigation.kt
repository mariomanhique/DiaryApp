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
import com.mariomanhique.profile.extractImagePath
import com.mariomanhique.ui.GalleryImage
import com.mariomanhique.util.fetchImageFromFirebase


const val profile_route = "profile_route"
fun NavController.navigateToProfile(navOptions: NavOptions?=null){
    this.navigate(profile_route, navOptions)
}
fun NavGraphBuilder.profileRoute(
    profileViewModel:ProfileViewModel,
    onDeleteClicked: (Boolean) -> Unit,
    onLogoutClicked: (Boolean) -> Unit
){

    composable(route = profile_route){

        ProfileScreen(
            profileViewModel = profileViewModel,
            onDeleteClicked = onDeleteClicked,
            onLogoutClicked = onLogoutClicked
        )
    }

}