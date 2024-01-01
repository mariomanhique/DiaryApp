package com.example.diaryapp

import android.os.Bundle
import android.graphics.Color
import androidx.activity.viewModels
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import com.example.diaryapp.ui.theme.DiaryAppTheme
import com.example.diaryapp.util.retryDeletingImageFromFirebase
import com.example.diaryapp.util.retryUploadingImageToFirebase
import com.google.firebase.FirebaseApp
import com.example.diaryapp.data.repository.imageRepo.ImageRepository
import com.example.diaryapp.MainActivityUiState.Loading
import com.example.diaryapp.MainActivityUiState.Success
import com.example.diaryapp.model.DarkThemeConfig.DARK
import com.example.diaryapp.model.DarkThemeConfig.LIGHT
import com.example.diaryapp.model.DarkThemeConfig.FOLLOW_SYSTEM
import com.example.diaryapp.model.ThemeBrand.DEFAULT
import com.example.diaryapp.model.ThemeBrand.ANDROID
import com.example.diaryapp.navigation.DiaryApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageRepository: ImageRepository

    @Inject
    lateinit var connectivity: NetworkConnectivityObserver

    val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }.collect()
            }
        }

        installSplashScreen().setKeepOnScreenCondition{
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window,false)
        FirebaseApp.initializeApp(this)
        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }
//            CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
                DiaryAppTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ){
                    DiaryApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        connectivity = connectivity
                        )
                }
//            }

        }

        cleanupCheck(
            scope = lifecycleScope,
            imageRepository = imageRepository
        )
    }

}

private fun cleanupCheck(
    scope: CoroutineScope,
  imageRepository: ImageRepository
) {
    scope.launch(Dispatchers.IO) {
        imageRepository.getAllImagesToUpload().distinctUntilChanged().collect{imagesList->
           imagesList.forEach { imageToUpload ->
               retryUploadingImageToFirebase(
                   imageToUpload = imageToUpload,
                   onSuccess = {
                       scope.launch(Dispatchers.IO) {
                           imageRepository.cleanupImageUp(imageId = imageToUpload.id)
                       }
                   }
               )
           }
       }

        imageRepository.getAllImagesToDelete()
            .distinctUntilChanged().collect{
            it.forEach { imageToDelete ->
                retryDeletingImageFromFirebase(
                    imageToDelete = imageToDelete,
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            imageRepository.cleanupImageD(imageId = imageToDelete.id)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Returns `true` if the Android theme should be used, as a function of the [uiState].
 */
@Composable
private fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> when (uiState.userData.themeBrand) {
        DEFAULT -> false
        ANDROID -> true
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> !uiState.userData.useDynamicColor
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeConfig) {
       FOLLOW_SYSTEM -> isSystemInDarkTheme()
       LIGHT -> false
       DARK -> true
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
