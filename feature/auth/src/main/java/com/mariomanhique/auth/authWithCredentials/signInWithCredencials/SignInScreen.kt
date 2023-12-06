package com.mariomanhique.auth.authWithCredentials.signInWithCredencials



import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import kotlinx.coroutines.launch
import com.mariomanhique.ui.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SignInScreen(
    navigateToHome: ()->Unit,
    navigateToSignUp:()->Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: AuthWithCredentialsViewModel = hiltViewModel(),
) {

    var loadingState by viewModel.loadingState


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

           Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .clip(RoundedCornerShape(30.dp))
                .padding(10.dp)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Sign In",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = emailValue.value,
                        onValueChange = {
                            emailValue.value = it
                                        },
                        label = { Text(text = "Email Address") },
                        placeholder = { Text(text = "Email Address") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .focusRequester(focusRequester = focusRequester),
                    )

                    OutlinedTextField(
                        value = passwordValue.value,
                        onValueChange = { passwordValue.value = it },
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
                        label = { Text("Password") },
                        placeholder = { Text(text = "Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .focusRequester(focusRequester = focusRequester),
                    )




                    Spacer(modifier = Modifier.padding(10.dp))

                    GoogleButton(loadingState = loadingState) {
                        if(emailValue.value.isEmpty() && passwordValue.value.isEmpty()){
                            Toast.makeText(context,"Fields can't be blank", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.signIn(
                                email = emailValue.value,
                                password = passwordValue.value,
                                onSuccess = {
                                    scope.launch {
                                        onShowSnackbar("Logged In Successfully",null)
                                        navigateToHome()
                                        viewModel.setLoading(false)
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        onShowSnackbar("Something wen wrong, check credentials",null)
                                    }
                                    viewModel.setLoading(false)
                                }
                            )
                            viewModel.setLoading(true)
                        }
                    }

                    Spacer(modifier = Modifier.padding(20.dp))
                    Text(
                        text = "Create An Account",
                        modifier = Modifier.clickable(onClick = {
                            navigateToSignUp()
                        })
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                }
            }
        }
    }
}
