package com.example.diaryapp.ui

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
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.navigateToSignIn
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.navigateToSignUp
import com.mariomanhique.auth.authWithCredentials.signUpWithCredentials.navigation.signUpNavigationRoute
import com.mariomanhique.home.navigation.diariesDestinationRoute
import com.mariomanhique.home.navigation.navigateToHome
import com.mariomanhique.profile.navigation.navigateToProfile
import com.mariomanhique.profile.navigation.profile_route
import com.mariomanhique.util.TopLevelDestination
import com.mariomanhique.util.TopLevelDestination.HOME
import com.mariomanhique.util.TopLevelDestination.PROFILE
import kotlinx.coroutines.flow.SharingStarted
import navigateToWrite
import write_navigation_route


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
    private val windowSizeClass: WindowSizeClass,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            diariesDestinationRoute -> HOME
            profile_route -> PROFILE
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
                HOME -> navController.navigateToHome(topLevelNavOptions)
                PROFILE -> navController.navigateToProfile(topLevelNavOptions)
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

}

