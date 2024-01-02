package com.mariomanhique.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mariomanhique.ui.R
import com.mariomanhique.util.fontFamily
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryAppBar(
    modifier: Modifier=Modifier,
    title: String,
    isProfileDestination: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked: ()->Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit
){

    val dialogState = rememberSheetState()
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Column(modifier =  modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontFamily = fontFamily(
                        fontName = "Chakra Petch",
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    )
                )
            }
        },
//        navigationIcon = {
//            IconButton(
//                onClick = {
//                    onMenuClicked()
//                }) {
//                Icon(imageVector = navigationIcon,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.onSurface
//
//                    )
//            }
//        },
        actions = {
            if(isProfileDestination){
                if(dateIsSelected){
                    IconButton(onClick = onDateReset) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }else{
                    IconButton(onClick = {
                        dialogState.show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }else{}
        }
    )

    CalendarDialog(
        state = dialogState,
        selection = CalendarSelection.Date{localDate->
        pickedDate = localDate
        onDateSelected(
            ZonedDateTime.of(
                pickedDate,
                LocalTime.now(),
                ZoneId.systemDefault()
            )
        )
    },
        config = CalendarConfig(monthSelection = true, yearSelection = true),
    )
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onDiariesDelete: () -> Unit,
    content: @Composable () -> Unit
){
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(
                            id = R.drawable.diary
                        ),
                        contentDescription ="",
                        modifier = Modifier.size(250.dp)
                    )
                }

                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Image(imageVector = Icons.Default.Delete,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Delete Diaries",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected =false,
                    onClick = onDiariesDelete
                )

                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Image(painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Sign Out",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected =false,
                    onClick = onSignOutClicked
                )


            }

        },
        content = {
                content()
        }
    )
}