package com.example.diaryapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.example.diaryapp.navigation.TopLevelDestination
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.mariomanhique.home.navigation.diariesDestinationRoute
import com.mariomanhique.home.navigation.navigateToHome
import com.mariomanhique.profile.navigation.navigateToProfile
import com.mariomanhique.profile.navigation.profile_route
import kotlinx.coroutines.CoroutineScope
import navigateToWrite


@Composable
fun rememberDiaryAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
):DiaryAppState{
    return remember(
        navController,
        windowSizeClass,
    ) {
        DiaryAppState(
            navController,
            windowSizeClass,
        )
    }
}
class DiaryAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,

) {


    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route){
            diariesDestinationRoute -> TopLevelDestination.HOME
            profile_route -> TopLevelDestination.PROFILE
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()


    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination){
        trace("Navigation: ${topLevelDestination.name}"){
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            val topLevelNavOptions = navOptions {

                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }

                launchSingleTop = true

                restoreState = true
            }

            when(topLevelDestination){
                TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
                TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
            }
        }
    }

    fun navigateToWrite(){
        navController.navigateToWrite()
    }

    fun navigateToSignIn(){
        navController.navigateToSignIn()
    }
}

