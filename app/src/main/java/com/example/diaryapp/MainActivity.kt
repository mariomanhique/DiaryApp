package com.example.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.diaryapp.data.database.ImageToUploadDao
import com.example.diaryapp.navigation.NavigationGraph
import com.example.diaryapp.navigation.Screen
import com.example.diaryapp.ui.theme.DiaryAppTheme
import com.example.diaryapp.util.retryDeletingImageFromFirebase
import com.example.diaryapp.util.retryUploadingImageToFirebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.example.diaryapp.data.database.ImageToDeleteDao
import com.example.diaryapp.data.repository.imageRepo.ImageRepository
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
                    val navController = rememberNavController()
                    //The startDestination is hard coded now, but then we will dynamically calculate based on user event
                    NavigationGraph(
                       startDestination = getStartDestination(),
                        navController = navController,
                        onDataLoaded = {
                            keepSplashOpened = false
                        }
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

private fun getStartDestination():String{
    val user = FirebaseAuth.getInstance().currentUser
    return if(user!=null) Screen.Home.route
    else Screen.SignIn.route
}
