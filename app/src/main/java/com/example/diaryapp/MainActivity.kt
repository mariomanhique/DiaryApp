package com.example.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
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
import com.example.diaryapp.navigation.DiaryApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageRepository: ImageRepository

    @Inject
    lateinit var connectivity: NetworkConnectivityObserver

    private var keepSplashOpened = true
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
               delay(500L)
                keepSplashOpened = false
            }
        }
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window,false)
        FirebaseApp.initializeApp(this)
        setContent {
            DiaryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    //The startDestination is hard coded now, but then we will dynamically calculate based on user event
                    DiaryApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        connectivity = connectivity
                        )
                }
            }
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
