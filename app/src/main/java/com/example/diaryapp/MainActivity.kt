package com.example.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.diaryapp.ui.DiaryApp
import com.mariomanhique.ui.theme.DiaryAppTheme
import com.google.firebase.FirebaseApp
import com.mariomanhique.database.imageRepo.ImageRepository
import com.mariomanhique.util.retryDeletingImageFromFirebase
import com.mariomanhique.util.retryUploadingImageToFirebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.diaryapp.MainActivityUiState.Loading
import com.example.diaryapp.MainActivityUiState.Success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageRepository: ImageRepository

    private var keepSplashOpened = true

    val viewModel: MainActivityViewModel by viewModels()

//    @Inject
//    lateinit var analyticsHelper: AnalyticsHelper



    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState
                    .onEach {
                        uiState = it
                }.collect()
            }
        }


        installSplashScreen().setKeepOnScreenCondition{
            when(uiState){
                Loading -> true
                is Success -> false
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window,false)
        FirebaseApp.initializeApp(this)
        setContent {
            DiaryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    DiaryApp(
                        windowSizeClass = calculateWindowSizeClass(this),
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
