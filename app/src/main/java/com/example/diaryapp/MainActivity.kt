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
import androidx.lifecycle.lifecycleScope
import com.example.diaryapp.ui.DiaryApp
import com.mariomanhique.ui.theme.DiaryAppTheme
import com.google.firebase.FirebaseApp
import com.mariomanhique.firestore.repository.imageRepo.ImageRepository
import com.mariomanhique.util.retryDeletingImageFromFirebase
import com.mariomanhique.util.retryUploadingImageToFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageRepository: ImageRepository

    private var keepSplashOpened = true
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        windowSizeClass = calculateWindowSizeClass(this),
                        onDataLoaded = {keepSplashOpened = false}
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
