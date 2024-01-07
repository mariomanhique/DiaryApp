package com.example.diaryapp.presentation.screens.auth.authWithCredentials

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.data.repository.authWithCredentials.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.diaryapp.model.Result

import javax.inject.Inject

@HiltViewModel
class AuthWithCredentialsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
):ViewModel() {

    var isAuthenticated = mutableStateOf(false)

    fun resetAuthState(){
        isAuthenticated.value = false
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit

    ){
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()){
                isAuthenticated.value = false
            }else {
                authRepository.signIn(
                    email = email,
                    password = password
                ).also {
                    when (it) {
                        is Result.Success -> {
                            onSuccess()
                            isAuthenticated.value = true
                        }

                        is Result.Loading -> {
                            isAuthenticated.value = false
                        }

                        is Result.Error -> {
                            it.exception?.let { exception -> onFailure(exception) }
                        }
                    }
                }
            }



        }
    }

    fun signUp(
        email: String,
        name: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ){
        viewModelScope.launch {
            //This check is done also on the screens that use the method, but I had to do it here for unit tests
            if (email.isEmpty() || name.isEmpty() || password.isEmpty()){
                isAuthenticated.value = false
            }else{
                isAuthenticated.value = true
                authRepository.signUp(
                    email = email,
                    name = name,
                    password = password
                ).also {
                    when(it){
                        is Result.Success ->{
                            onSuccess()
                            isAuthenticated.value = true
                        }
                        is Result.Loading -> {
                            isAuthenticated.value = false
                        }

                        is Result.Error -> {
                            it.exception?.let { exception -> onFailure(exception) }
                        }
                    }
                }
            }
        }
    }
}