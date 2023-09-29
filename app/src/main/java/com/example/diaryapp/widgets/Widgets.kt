package com.example.diaryapp.widgets

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diaryapp.icons.DiaryIcons
import com.example.diaryapp.util.DateConverter
import com.example.diaryapp.util.fontFamily
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryAppBar(
    title: String,
    modifier: Modifier=Modifier,
    navigationIcon: ImageVector,
    actionIcon: ImageVector,
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked: ()->Unit,
){

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
        navigationIcon = {
            IconButton(
                onClick = {
                    onMenuClicked()
                }) {
                Icon(imageVector = navigationIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface

                    )

            }
        },
        actions = {
            Icon(
                imageVector = actionIcon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
                            id = DiaryIcons.appLogo
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
                            Image(painter = painterResource(id = DiaryIcons.googleLogo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "SignOut",
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