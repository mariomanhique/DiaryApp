package com.example.diaryapp.ui

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.example.diaryapp.navigation.NavigationHost
import com.mariomanhique.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.mariomanhique.auth.authWithCredentials.signInWithCredencials.navigation.signInNavigationRoute
import com.mariomanhique.home.DiariesViewModel
import com.mariomanhique.home.DiaryAppBar
import com.mariomanhique.home.NavigationDrawer
import com.mariomanhique.home.navigation.diariesDestinationRoute
import com.mariomanhique.ui.components.DisplayAlertDialog
import com.mariomanhique.util.TopLevelDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DiaryApp(
    windowSizeClass: WindowSizeClass,
    onDataLoaded: () -> Unit
){
    DiaryContent(
        windowSizeClass = windowSizeClass,
        onDataLoaded = onDataLoaded
        )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryContent(
    windowSizeClass: WindowSizeClass,
    appState: DiaryAppState  = rememberDiaryAppState(
        windowSizeClass = windowSizeClass),
    onDataLoaded: () -> Unit,
    authViewModel: AuthWithCredentialsViewModel = hiltViewModel(),
    diariesViewModel: DiariesViewModel = hiltViewModel()
){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val destination = appState.currentTopLevelDestination
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val diaries = diariesViewModel.diaries.collectAsStateWithLifecycle().value



    var user = authViewModel.user
    var signOutDialogState by remember { mutableStateOf(false) }
    var deleteAllDialogOpened by remember { mutableStateOf(false) }
    val context = LocalContext.current
//
//    NavigationDrawer(
//        drawerState = drawerState,
//        onDiariesDelete = {
//            deleteAllDialogOpened = true
//        },
//        onSignOutClicked = {
//            signOutDialogState = true
//        }
//    ) {
    Scaffold(
        modifier = Modifier
            .semantics {
                testTagsAsResourceId = true
            }
//            .background(MaterialTheme.colorScheme.surface)
//            .navigationBarsPadding()
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
                if (destination != null){
                    FloatingActionButton(
                        modifier= Modifier
                            .padding(4.dp),
                        onClick =  appState::navigateToWrite
                    ) {
                        Box(
                            modifier = Modifier
                        ) {
                            Icon(
                                modifier = Modifier,
                                imageVector = Icons.Default.Add,
                                contentDescription =""
                            )
                        }
                    }
                }
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                if (destination != null){
                        DiaryBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = Modifier.testTag("DiaryBottomBar"),
                        )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = ((-0).dp), y = (-0).dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(modifier= Modifier
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.surface)
                        ){
//                            FloatingActionButton(
//                                modifier= Modifier
//                                    .size(50.dp)
//                                    .padding(4.dp)
//                                    .align(Alignment.TopCenter)
//                                    .clip(CircleShape)
//                                    .offset(x = ((-0).dp), y = (-0).dp),
//                                onClick =  appState::navigateToWrite
//                            ) {
//                                Box(
//                                    modifier = Modifier
//                                        .clip(CircleShape)
//                                ) {
//                                    Icon(
//                                        modifier = Modifier,
//                                        imageVector = Icons.Default.Add,
//                                        contentDescription =""
//                                    )
//                                }
//                            }
                        }

                    }
                }

            }
        },
    ){paddingValues->
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
                    DiaryNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier
                            .testTag("DiaryNavRail")
                            .safeDrawingPadding(),
                    )
                }

            Column(modifier = Modifier.fillMaxSize()) {
                if(destination != null){
                    DiaryAppBar(
                        title = stringResource(
                            if(destination == TopLevelDestination.HOME) R.string.home
                            else R.string.profile),
                        isProfileDestination = destination != TopLevelDestination.PROFILE,
                        navigationIcon = Icons.Rounded.Menu,
                        scrollBehavior = scrollBehavior,
                        onMenuClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        dateIsSelected = diariesViewModel.dateIsSelected,
                        onDateSelected = {
                            diariesViewModel.getDiaries(it)
                        },
                        onDateReset = {
                            diariesViewModel.getDiaries()
                        }
                    )
                }
                NavigationHost(
                    appState = appState,
                    onDataLoaded = onDataLoaded,
                    paddingValues = paddingValues,
                    windowSizeClass = windowSizeClass,
                    diaries = diaries,
                    startDestination = if(user!=null)  diariesDestinationRoute else  signInNavigationRoute
                )
            }
        }

     }


//    }

    DisplayAlertDialog(
        title = "Sign Out",
        message = "Are you sure you want to sign out?",
        dialogOpened = signOutDialogState,
        onCloseDialog = {
            signOutDialogState = false
        },
        onYesClicked = {
            signOutDialogState      = false
            scope.launch(Dispatchers.IO) {
                authViewModel.signOut()
                withContext(Dispatchers.Main){
                    appState.navigateToSignIn()
                    drawerState.close()
                }
            }
        }
    )

    DisplayAlertDialog(
        title = "Delete All Diaries",
        message = "Are you sure you want to delete all diaries?",
        dialogOpened = deleteAllDialogOpened,
        onCloseDialog = {
            deleteAllDialogOpened = false
        },
        onYesClicked = {
            deleteAllDialogOpened = false
            scope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main){
                    diariesViewModel.deleteAllDiaries(
                        onSuccess = {
                            if(it){
                                scope.launch {
                                    Toast.makeText(context,"Diaries Deleted", Toast.LENGTH_SHORT).show()
                                    drawerState.close()
                                }
                            }

                        },
                        onError = {error->

                            scope.launch {
                                if (error.message == "No internet connection."){
                                    Toast.makeText(context,"You need internet connection " +
                                            "to perform this action", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(context,"${error.message}", Toast.LENGTH_SHORT).show()
                                }
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    )
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
                        modifier = Modifier.size(35.dp)
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
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
       modifier = Modifier
           .heightIn(max = 80.dp),
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