package com.example.diaryapp.util

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.diaryapp.R

fun fontFamily(fontName:String,fontWeight: FontWeight,fontStyle: FontStyle):FontFamily{

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontNam = GoogleFont(fontName)

   return FontFamily(
        Font(
            googleFont = fontNam,
            fontProvider=provider,
            weight = fontWeight,
            style= fontStyle)
    )


}