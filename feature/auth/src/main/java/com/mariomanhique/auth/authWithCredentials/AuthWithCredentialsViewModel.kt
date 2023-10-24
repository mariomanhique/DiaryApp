package com.mariomanhique.auth.authWithCredentials

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariomanhique.firestore.repository.authWithCredentials.AuthRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.mariomanhique.auth.SignInState
import com.mariomanhique.firestore.repository.profileRepository.ProfileRepository
import com.mariomanhique.util.model.SignInResult
import com.mariomanhique.util.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthWithCredentialsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
):ViewModel() {

    private val _state = MutableStateFlow(SignInState())
     val user = authRepository.currentUser
    var loadingState = mutableStateOf(false)
        private set

    val state = _state.asStateFlow()


    fun setLoading(loading:Boolean){
        loadingState.value = loading
    }
    fun getMongoUser(): User {
        return authRepository.mongoCurrentUser!!
    }

    private fun saveProfile(
        name:String,
        profile:String
    ){
//        viewModelScope.launch {
           profileRepository.saveProfile(
               name = name,
               profile = profile
           )

//        }
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
            run {
                saveProfile(name = name, user?.photoUrl.toString())
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

    fun signInWithMongoAtlas(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ){
        viewModelScope.launch {
            try {
                val result = authRepository
                    .mongoSignIn(
                        email = email,
                        password = password
                    )?.loggedIn
                withContext(Dispatchers.Main) {
                    if(result==true){
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    fun signUpWithMongoAtlas(
        email: String,
        password: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        viewModelScope.launch {


            try {
                authRepository.mongoSignUp(
                    email = email,
                    password = password
                )
                withContext(Dispatchers.Main){
                    onSuccess(true)//TODO change this to a dynamic value

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            authRepository.logout()
            authRepository.logoutFromMongo()
        }
    }

    private fun signOutFromMongo(){
        viewModelScope.launch {
            authRepository.logoutFromMongo()
        }
    }

}