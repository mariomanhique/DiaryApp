package com.mariomanhique.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mariomanhique.profile.ProfileScreen
import com.mariomanhique.profile.ProfileViewModel


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