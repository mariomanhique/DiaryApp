package com.example.diaryapp.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthWithCredentialsViewModel = hiltViewModel()
){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            
            Text(text = "Home")
        }
    }
}