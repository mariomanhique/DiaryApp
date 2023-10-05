package com.mariomanhique.util

import com.mariomanhique.util.Constants.WRITE_SCREEN_ARG_KEY

sealed class Screen(val route: String){
    data object SignIn:Screen(route = "sign_in")
    data object SignUp:Screen(route = "sign_up")
    data object Home:Screen(route = "home_screen")
    data object Write:Screen(route = "write_screen?$WRITE_SCREEN_ARG_KEY=" +
            "{$WRITE_SCREEN_ARG_KEY}") {//The argument that we will define is optional.
        fun passDiaryId(diaryId:String)=
            "write_screen?$WRITE_SCREEN_ARG_KEY=$diaryId"


    }
}
