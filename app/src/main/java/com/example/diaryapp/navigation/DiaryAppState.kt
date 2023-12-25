package com.example.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.example.diaryapp.presentation.screens.home.navigation.homeRoute
import com.example.diaryapp.presentation.screens.home.navigation.navigateToHome
import com.example.diaryapp.presentation.screens.profile.navigation.navigateToProfile
import com.example.diaryapp.presentation.screens.profile.navigation.profile_route
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import navigateToWrite
import write_navigation_route


@Composable
fun rememberDiaryAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
): DiaryAppState {
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
    private val windowSizeClass: WindowSizeClass,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeRoute -> TopLevelDestination.HOME
            profile_route -> TopLevelDestination.PROFILE
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar


    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()




    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}"){
                val topLevelNavOptions = navOptions {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
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

        val navOptions = navOptions {
            popUpTo(write_navigation_route){
                inclusive = false
            }
            launchSingleTop = true

            restoreState = true
        }
        navController.navigateToWrite(navOptions)
    }

    fun navigateToSignIn(){

        val navOptions = navOptions {
            popUpTo(write_navigation_route){
                inclusive = false
            }
            launchSingleTop = true

            restoreState = true
        }
        navController.navigateToSignIn(navOptions)
    }

}

