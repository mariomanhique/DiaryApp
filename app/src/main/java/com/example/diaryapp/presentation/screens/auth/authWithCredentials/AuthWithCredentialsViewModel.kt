package com.example.diaryapp.presentation.screens.auth.authWithCredentials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.data.signInWithCredentialsRepository.AuthRepository
import com.example.diaryapp.data.signInWithCredentialsRepository.utils.await
import com.example.diaryapp.presentation.screens.auth.SignInResult
import com.example.diaryapp.presentation.screens.auth.SignInState
import com.example.diaryapp.presentation.screens.auth.UserData
import com.example.diaryapp.util.Constants
import com.example.diaryapp.util.Constants.App_ID
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthWithCredentialsViewModel @Inject constructor(private val authRepository: AuthRepository):ViewModel() {

     private val _state = MutableStateFlow(SignInState())

    val state = _state.asStateFlow()

    init {
        signOut()
    }



    fun getCurrentUser() = authRepository.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = null
        )
    }

     suspend fun getToken() = authRepository.currentUser?.run {
       getIdToken(true).await().token
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

    fun signIn(email:String,password:String)= viewModelScope.launch {

         val result = try {

             val user = authRepository.signIn(email,password)

             SignInResult(
                    data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                    errorMessage = null
            )


            }catch (e: FirebaseAuthException){
              e.printStackTrace()
//              if(e is CancellationException) throw e
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

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
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

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        viewModelScope.launch {
          try {
              val result = App.create(App_ID).login(
                        Credentials.google(
                            token = tokenId,
                            GoogleAuthType.ID_TOKEN
                        )
                    ).loggedIn
                withContext(Dispatchers.Main) {
                    onSuccess(result)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    private fun signOut(){
        authRepository.logout()
    }

}