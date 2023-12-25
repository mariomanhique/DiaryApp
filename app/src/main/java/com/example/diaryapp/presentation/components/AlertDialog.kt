package com.example.diaryapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

//I will later change state to DialogState data class
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    dialogOpened: Boolean,
    onCloseDialog: () -> Unit,
    onYesClicked: () -> Unit
){

    if(dialogOpened){
        AlertDialog(
            onDismissRequest =onCloseDialog,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
//
//            Dialog(onDismissRequest = onCloseDialog,
//                properties = DialogProperties(
//                    usePlatformDefaultWidth = false
//                )
//            ) {
                Card(
                    elevation = CardDefaults.cardElevation(5.dp),
                    shape = CircleShape.copy(all = CornerSize(15.dp)),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .border(1.dp, color = Color.White, shape = RoundedCornerShape(15.dp)),
//            colors = CardDefaults.cardColors(AppColors.mGreen)

                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(25.dp))
                    {
                        Text(text = title,
                            Modifier.align(alignment = Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 25.sp)

                        Column(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = message,
                                Modifier
                                    .padding(bottom = 10.dp)
                                    .align(alignment = Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 1.sp,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )



                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {


                                Button(
                                    onClick = {
                                        onCloseDialog()
                                    },
                                    border = BorderStroke(width = 1.dp,
                                        brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.onSurface,
                                            MaterialTheme.colorScheme.onSurface
                                            )
                                    )),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    )
                                ) {
                                    Text(text = "No",
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface)

                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Button(onClick = {
                                    onYesClicked()
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    border = BorderStroke(width = 1.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.onSurface,
                                                MaterialTheme.colorScheme.onSurface
                                            )
                                        ))

                                ) {
                                    Text(text = "Yes",
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }



                        }
                    }
                }
            }

        }

}