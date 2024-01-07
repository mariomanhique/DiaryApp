package com.example.diaryapp.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.diaryapp.R
import com.example.diaryapp.connectivity.ConnectivityObserver
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import com.example.diaryapp.presentation.components.DiaryBackground
import com.example.diaryapp.presentation.components.DiaryGradientBackground
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.signInWithCredencials.navigation.signInNavigationRoute
import com.example.diaryapp.presentation.screens.home.navigation.homeRoute
import com.example.diaryapp.presentation.screens.settingsDialog.SettingsDialog
import com.example.diaryapp.ui.theme.GradientColors
import com.example.diaryapp.ui.theme.LocalGradientColors
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DiaryApp(
    windowSizeClass: WindowSizeClass,
    connectivity: NetworkConnectivityObserver,

){
    DiaryContent(
        windowSizeClass = windowSizeClass,
        connectivity = connectivity
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryContent(
    windowSizeClass: WindowSizeClass,
    connectivity: NetworkConnectivityObserver,
    appState: DiaryAppState = rememberDiaryAppState(
        windowSizeClass = windowSizeClass,
        connectivity = connectivity,

    ),
        authViewModel: AuthWithCredentialsViewModel = hiltViewModel(),
){

    val shouldShowGradientBackground =
        appState.currentTopLevelDestination == TopLevelDestination.HOME

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val destination = appState.currentTopLevelDestination
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    var isNetworkAvailable by remember {
        mutableStateOf(true)
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var signOutDialogState by remember { mutableStateOf(false) }
    var deleteAllDialogOpened by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showSettingsDialog by remember {
        mutableStateOf(false)
    }

    DiaryBackground {
       DiaryGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {

           val notConnectedMessage = stringResource(R.string.not_connected)
           val connectedMessage = stringResource(R.string.online)
           LaunchedEffect(key1 = isOffline) {
               when (isOffline) {
                   ConnectivityObserver.Status.Available -> {
                       isNetworkAvailable = true
                       snackbarHostState.showSnackbar(
                           message = connectedMessage,
                           duration = Short,
                       )
                   }

                   ConnectivityObserver.Status.Unavailable -> {
                       isNetworkAvailable = false
                       snackbarHostState.showSnackbar(
                           message = notConnectedMessage,
                           duration = SnackbarDuration.Indefinite,
                       )
                   }

                   ConnectivityObserver.Status.Losing -> {
                       isNetworkAvailable = false
                       snackbarHostState.showSnackbar(
                           message = notConnectedMessage,
                           duration = SnackbarDuration.Indefinite,
                       )
                   }

                   ConnectivityObserver.Status.Lost -> {
                       isNetworkAvailable = false
                       snackbarHostState.showSnackbar(
                           message = notConnectedMessage,
                           duration = SnackbarDuration.Indefinite,
                       )
                   }
               }
           }

           if (showSettingsDialog) {
               SettingsDialog(
                   onDismiss = { showSettingsDialog = false },
               )
           }

           Scaffold(
               modifier = Modifier
                   .semantics {
                       testTagsAsResourceId = true
                   }
                   .background(MaterialTheme.colorScheme.surface)
//            .navigationBarsPadding()
                   .imePadding()
                   .statusBarsPadding(),
               containerColor = Color.Transparent,
               contentColor = MaterialTheme.colorScheme.onBackground,
               contentWindowInsets = WindowInsets(0, 0, 0, 0),
               snackbarHost = { SnackbarHost(snackbarHostState) },
               floatingActionButton = {
                   if (destination != null) {
                       FloatingActionButton(
                           modifier = Modifier
                               .padding(4.dp),
                           onClick = appState::navigateToWrite
                       ) {
                           Box(
                               modifier = Modifier
                           ) {
                               Icon(
                                   modifier = Modifier,
                                   imageVector = Icons.Default.Add,
                                   contentDescription = ""
                               )
                           }
                       }
                   }
               },
               bottomBar = {
                   if (appState.shouldShowBottomBar) {
                       if (destination != null) {
                           DiaryBottomBar(
                               destinations = appState.topLevelDestinations,
                               onNavigateToDestination = appState::navigateToTopLevelDestination,
                               currentDestination = appState.currentDestination,
                               modifier = Modifier.testTag("DiaryBottomBar"),
                           )
                       }
                   }
               },
           ) { paddingValues ->
               Row(
                   Modifier
                       .fillMaxSize()
                       .padding(paddingValues)
                       .consumeWindowInsets(paddingValues)
                       .windowInsetsPadding(
                           WindowInsets.safeDrawing.only(
                               WindowInsetsSides.Horizontal,
                           ),
                       ),
               ) {

                   if (appState.shouldShowNavRail) {
                       if (destination != null) {
                           DiaryNavRail(
                               destinations = appState.topLevelDestinations,
                               onNavigateToDestination = appState::navigateToTopLevelDestination,
                               currentDestination = appState.currentDestination,
                               modifier = Modifier
                                   .testTag("DiaryNavRail")
                                   .safeDrawingPadding(),
                           )
                       }
                   }

                   Column(modifier = Modifier.fillMaxSize()) {
                       if (destination != null) {
//                    DiaryAppBar(
//                        title = stringResource(
//                            if(destination == TopLevelDestination.HOME) R.string.home
//                            else R.string.profile),
//                        isProfileDestination = destination != TopLevelDestination.PROFILE,
//                        navigationIcon = Icons.Rounded.Menu,
//                        scrollBehavior = scrollBehavior,
//                        onMenuClicked = {},
//                        dateIsSelected = homeViewModel.dateIsSelected,
//                        onDateSelected = {
//                            homeViewModel.getDiaries(it)
//                        },
//                        onDateReset = {
//                            homeViewModel.getDiaries()
//                        }
//                    )
                       }

                       val user = FirebaseAuth.getInstance().currentUser
                       NavigationHost(
                           onShowSnackbar = { message, action ->
                               snackbarHostState.showSnackbar(
                                   message = message,
                                   actionLabel = action,
                                   duration = Short
                               ) == ActionPerformed
                           },
                           isNetworkAvailable = isNetworkAvailable,
                           appState = appState,
                           onDialogOpened = {
                               showSettingsDialog = true
                           },
                           paddingValues = paddingValues,
                           shouldShowLandscape = appState.shouldShowNavRail,
                           startDestination = if (user != null) homeRoute else signInNavigationRoute
                       )
                   }
               }

           }
       }
    }

}

@Composable
fun DiaryBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    ){
    DiaryNavigationBar(
        modifier = modifier
    ) {

        destinations.forEach {destination->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            Log.d("Test Nav", "NiaBottomBar:$destination")
            DiaryNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = Modifier,
            )

        }
    }

}


@Composable
fun DiaryNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
       modifier = Modifier,
        contentColor = DiaryNavigationDefaults.navigationContentColor(),
        tonalElevation = 3.dp,
        content = content,
    )
}


@Composable
fun RowScope.DiaryNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = false,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = DiaryNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = DiaryNavigationDefaults.navigationContentColor(),
            selectedTextColor = DiaryNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = DiaryNavigationDefaults.navigationContentColor(),
            indicatorColor = DiaryNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}


object DiaryNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}


@Composable
private fun DiaryNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    DiaryNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            DiaryNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun DiaryNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = DiaryNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = DiaryNavigationDefaults.navigationContentColor(),
            selectedTextColor = DiaryNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = DiaryNavigationDefaults.navigationContentColor(),
            indicatorColor = DiaryNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun DiaryNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = DiaryNavigationDefaults.navigationContentColor(),
        header = header,
        content = content,
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false