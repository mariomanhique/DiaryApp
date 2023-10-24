package com.mariomanhique.util

import com.mariomanhique.util.Constants.WRITE_SCREEN_ARG_KEY

sealed class Screen(val route: String){
    data object Write:Screen(route = "write_navigation_route?$WRITE_SCREEN_ARG_KEY=" +
            "{$WRITE_SCREEN_ARG_KEY}") {//The argument that we will define is optional.
        fun passDiaryId(diaryId:String)=
            "write_navigation_route?$WRITE_SCREEN_ARG_KEY=$diaryId"


    }
}
