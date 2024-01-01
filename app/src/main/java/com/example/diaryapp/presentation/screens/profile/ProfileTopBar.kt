package com.example.diaryapp.presentation.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.diaryapp.R
import com.example.diaryapp.util.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
){
    CenterAlignedTopAppBar(
        navigationIcon = {
        },

        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.profile),
                   fontFamily = fontFamily(
                       fontName = "Chakra Petch",
                       fontWeight = FontWeight.Bold,
                       fontStyle = FontStyle.Normal
                   )
                )
            }
        },

    )
}
