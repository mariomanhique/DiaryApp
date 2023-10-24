package com.mariomanhique.util.model

data class UserData(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?

)
