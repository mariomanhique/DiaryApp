package com.mariomanhique.firestore.repository.authWithCredentials


import com.example.diaryapp.data.repository.authWithCredentials.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.mariomanhique.firestore.repository.authWithCredentials.AuthRepository
import com.mariomanhique.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val mongoAuth: App.Companion
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val mongoCurrentUser: User?
        get() = mongoAuth.create(Constants.App_ID).currentUser

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
      return try{

          firebaseAuth.signInWithEmailAndPassword(email,password).await().user

      }catch (e:FirebaseAuthException){
          e.printStackTrace()
         null
      }

    }

    override suspend fun signUp(email: String, password: String,name: String): FirebaseUser? {
        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await().user
            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

            result

        }catch (e:FirebaseAuthException){
            e.printStackTrace()
            null
        }
    }

    override suspend fun mongoSignIn(email: String, password: String): User?{
        return mongoAuth
            .create(Constants.App_ID)
            .login(Credentials.emailPassword(
               email = email,
                password = password
            ))
    }

    override suspend fun mongoSignUp(email: String, password: String) {
        runBlocking {
            mongoAuth.create(Constants.App_ID)
                .emailPasswordAuth.registerUser(email = email, password = password)
        }



    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun logoutFromMongo() {
        mongoAuth.create(Constants.App_ID).currentUser?.logOut()
    }
}