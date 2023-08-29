package com.example.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.diaryapp.navigation.NavigationGraph
import com.example.diaryapp.navigation.Screen
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.example.diaryapp.ui.theme.DiaryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    //The startDestination is hard coded now, but then we will dynamically calculate based on user event

                    val viewModel: AuthWithCredentialsViewModel = hiltViewModel()
                    LaunchedEffect(key1 = Unit) {
                        if(viewModel.getCurrentUser() != null) {
                            navController.navigate(Screen.Home.route)
                        }
                    }
                    NavigationGraph(
                       startDestination = Screen.SignIn.route,
                        navController = navController
                    )


                }
            }
        }
    }

}
