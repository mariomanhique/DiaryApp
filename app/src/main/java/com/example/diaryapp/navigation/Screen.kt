package com.example.diaryapp.navigation

import com.example.diaryapp.util.Constants.WRITE_SCREEN_ARG_KEY

sealed class Screen(val route: String){
    object SignIn:Screen(route = "sign_in")
    object SignUp:Screen(route = "sign_up")
    object Home:Screen(route = "home_screen")
    object Write:Screen(route = "write_screen?$WRITE_SCREEN_ARG_KEY=" +
            "{$WRITE_SCREEN_ARG_KEY}") {//The argument that we will define is optional.
        fun passDiaryId(diaryId:String)=
            "write_screen?$WRITE_SCREEN_ARG_KEY=$diaryId"


    }
}
