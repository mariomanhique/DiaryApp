package com.example.diaryapp.util
import android.util.Base64

object Base64Custom {

    fun encodeToBase64(text: String?): String {
        return Base64.encodeToString(text!!.toByteArray(), Base64.DEFAULT)
            .replace("(\\n|\\r)".toRegex(), "")
    }

    fun decodeFromBase64(encodedText: String?): String {
        return String(Base64.decode(encodedText, Base64.DEFAULT))
    }
}