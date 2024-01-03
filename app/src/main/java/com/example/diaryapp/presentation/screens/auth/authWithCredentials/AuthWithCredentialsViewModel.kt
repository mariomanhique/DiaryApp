package com.example.diaryapp.presentation.screens.auth.authWithCredentials

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.connectivity.ConnectivityObserver.Status.Unavailable
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import com.example.diaryapp.presentation.screens.auth.SignInResult
import com.example.diaryapp.presentation.screens.auth.SignInState
import com.example.diaryapp.presentation.screens.auth.UserData
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthWithCredentialsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val connectivity: NetworkConnectivityObserver,
):ViewModel() {

    private var _network by mutableStateOf(Unavailable)
    val network = _network
    private val _state = MutableStateFlow(SignInState())
    var loadingState = mutableStateOf(false)
        private set

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            connectivity.observe().collect{
                _network = it
            }
        }
    }


    fun setLoading(loading:Boolean){
        loadingState.value = loading
    }

    private fun onSignInResult(result: SignInResult){
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState(){
        _state.update {
            SignInState()
        }
    }

    fun signIn(
        email:String,
        password:String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )= viewModelScope.launch {

        val result = try {
            val user = authRepository.signIn(email,password)

            if ( user!= null){
                onSuccess()
                SignInResult(
                    data = user.run {
                        UserData(
                            userId = uid,
                            username = displayName.toString(),
                            profilePictureUrl = photoUrl.toString()
                        )
                    },
                    errorMessage = null
                )
            }else{
                onError(Exception("Error authenticating user"))
                SignInResult(
                    null,
                    errorMessage = Exception("Error authenticating user").message
                )
            }

        }catch (e: FirebaseAuthException){
            e.printStackTrace()
            SignInResult(
                null,
                errorMessage = e.message
            )

        }
            onSignInResult(result = result)
    }

    fun signUp(name:String,email:String,password: String) = viewModelScope.launch {

        val result = try {

            val user = authRepository.signUp(email=email, password = password,name=name)
            run {
                Log.d("SignUp", "signUp: ${user?.displayName} ")
                SignInResult(
                    data = user?.run {
                        UserData(
                            userId = uid,
                            username = displayName.toString(),
                            profilePictureUrl = photoUrl.toString()
                        )
                    },
                    errorMessage = null
                )
            }


        }catch (e: FirebaseAuthException){
            e.printStackTrace()
//            if(e is CancellationException) throw e
            SignInResult(
                null,
                errorMessage = e.message
            )
        }
        onSignInResult(result = result)
    }


    fun signOut(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }



}