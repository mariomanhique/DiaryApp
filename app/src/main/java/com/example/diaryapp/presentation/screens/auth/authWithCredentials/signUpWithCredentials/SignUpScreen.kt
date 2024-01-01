package com.example.diaryapp.presentation.screens.auth.authWithCredentials.signUpWithCredentials


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import kotlinx.coroutines.launch
import com.example.diaryapp.R
import com.example.diaryapp.presentation.components.GoogleButton


@Composable
internal fun SignUpWithCredentials(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    isNetworkAvailable: Boolean,
    viewModel: AuthWithCredentialsViewModel = hiltViewModel(),
    navigateToHome:()->Unit,
    navigateToSignIn:()->Unit,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val loadingState by viewModel.loadingState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    var textFieldEnabled by remember {
        mutableStateOf(true)
    }
    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }







    Spacer(modifier = Modifier.padding(20.dp))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(id = R.drawable.diary),
            contentDescription = "logo"
        )

        Text(
            modifier = Modifier.paddingFromBaseline(top =50.dp, bottom = 50.dp),
            text = "Sign Up",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            fontSize = 30.sp
        )
        OutlinedTextField(
            value = nameValue,
            enabled = isNetworkAvailable,
            onValueChange = { nameValue = it },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        OutlinedTextField(
            value = emailValue,
            enabled = isNetworkAvailable,
            onValueChange = { emailValue = it },
            label = { Text(text = "Email Address") },
            placeholder = { Text(text = "Email Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )


        OutlinedTextField(
            value = passwordValue,
            enabled = isNetworkAvailable,
            onValueChange = { passwordValue = it },
            label = { Text(text = "Password") },
            placeholder = { Text(text = "Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.password_eye),
                        contentDescription = "",
                        tint = if(passwordVisibility) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                scope.launch {
                    onShowSnackbar("Successful Signed Up",null)
                }
                navigateToHome()
            }
            viewModel.resetState()
        }
        GoogleButton(
            primaryText = "Sign Up",
            loadingState = loadingState
        ) {
            if (emailValue.isNotEmpty() && passwordValue.isNotEmpty() && nameValue.isNotEmpty()) {
                viewModel.signUp(
                    name = nameValue,
                    email = emailValue,
                    password = passwordValue
                )
                textFieldEnabled = false
                viewModel.setLoading(true)
                viewModel.resetState()
            } else{
                Toast.makeText(context,"Fields can't be blank", Toast.LENGTH_SHORT).show()
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