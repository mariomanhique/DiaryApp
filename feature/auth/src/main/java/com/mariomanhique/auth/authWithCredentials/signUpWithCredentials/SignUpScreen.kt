package com.mariomanhique.auth.authWithCredentials.signUpWithCredentials


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mariomanhique.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.mariomanhique.ui.components.GoogleButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mariomanhique.auth.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpWithCredentials(
    viewModel: AuthWithCredentialsViewModel = hiltViewModel(),
    navigateToHome:()->Unit,
    navigateToSignIn:()->Unit,
    ){

    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val loadingState by viewModel.loadingState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val nameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }

    Scaffold (modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .imePadding()
        .navigationBarsPadding()
        .statusBarsPadding()) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                   contentAlignment = Alignment.TopCenter
            ) {
                Image(painter = painterResource(id = com.mariomanhique.ui.R.drawable.logo), contentDescription = "")

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.70f)
                    .clip(RoundedCornerShape(30.dp))
                    .padding(10.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up", fontSize = 30.sp,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                    Spacer(modifier = Modifier.padding(20.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = nameValue.value,
                            onValueChange = { nameValue.value = it },
                            label = { Text(text = "Name") },
                            placeholder = { Text(text = "Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )

                        OutlinedTextField(
                            value = emailValue.value,
                            onValueChange = { emailValue.value = it },
                            label = { Text(text = "Email Address") },
                            placeholder = { Text(text = "Email Address") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )


                        OutlinedTextField(
                            value = passwordValue.value,
                            onValueChange = { passwordValue.value = it },
                            label = { Text(text = "Password") },
                            placeholder = { Text(text = "Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisibility.value = !passwordVisibility.value
                                }) {
                                    Icon(
                                        painter = painterResource(id = com.mariomanhique.ui.R.drawable.password_eye),
                                        contentDescription = "",
                                        tint = if (passwordVisibility.value) Color.White else Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                            else PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                viewModel.signUpWithMongoAtlas(
                                    email = emailValue.value,
                                    password = passwordValue.value,
                                    onSuccess = { isSingInSuccess ->
                                        if (isSingInSuccess) {
                                            viewModel.signInWithMongoAtlas(
                                                emailValue.value,
                                                passwordValue.value,
                                                onSuccess = {
                                                    navigateToHome()
                                                    viewModel.setLoading(false)
                                                },
                                                onError = {
                                                    viewModel.setLoading(false)
                                                    scope.launch {
                                                        withContext(Dispatchers.Main){
                                                            snackbarHostState.showSnackbar(
                                                                message = it.message.toString(),
                                                                duration = SnackbarDuration.Short
                                                            )
                                                        }
                                                    }
                                                    //SnackBarHost
                                                }
                                            )
                                        }
                                    },
                                    onError = {
                                        viewModel.setLoading(false)
                                        //SnackBarHost
                                    }
                                )
                            }
                            viewModel.resetState()
                        }
                        GoogleButton(
                            loadingState = loadingState
                        ) {
                            if (emailValue.value.isNotEmpty() && passwordValue.value.isNotEmpty()) {
                                viewModel.signUp(
                                    name = nameValue.value,
                                    email = emailValue.value,
                                    password = passwordValue.value
                                )
                                viewModel.setLoading(true)
                                viewModel.resetState()
                            }
                        }
                        Spacer(modifier = Modifier.padding(20.dp))
                        Text(
                            text = "Login Instead",
                            modifier = Modifier.clickable(onClick = {
                                navigateToSignIn()
                            })
                        )
                        Spacer(modifier = Modifier.padding(20.dp))
                    }
                }
            }
        }
    }
}