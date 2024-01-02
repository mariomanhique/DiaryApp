package com.mariomanhique.firestore.repository.authWithCredentials


import com.mariomanhique.firestore.repository.authWithCredentials.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.mariomanhique.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val mongoAuth: App.Companion,
    private val firestore: FirebaseFirestore
): AuthRepository {

    private val ref = firestore.collection("profile")
    val firebaseAuth = FirebaseAuth.getInstance()


    override val mongoCurrentUser: User?
        get() = mongoAuth.create(Constants.App_ID).currentUser

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
      return try{

          FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await().user

      }catch (e:FirebaseAuthException){
          e.printStackTrace()
         null
      }

    }

    override suspend fun signUp(email: String, password: String,name: String): FirebaseUser? {
        return try {

            val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await().user
            result?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

            result?.uid?.let {userId->
                ref.document(userId).set(
                    com.mariomanhique.util.model.UserData(
                        userId,
                        username = name,
                        profilePictureUrl = ""
                    )
                )
            }

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
        FirebaseAuth.getInstance().signOut()
    }

    override suspend fun logoutFromMongo() {
        mongoAuth.create(Constants.App_ID).currentUser?.logOut()
    }
}