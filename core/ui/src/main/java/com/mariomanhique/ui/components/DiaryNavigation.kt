package com.mariomanhique.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mariomanhique.ui.theme.DiaryAppTheme
import com.mariomanhique.ui.R

@Composable
fun DiaryNavigation(
    onHomeClicked: () -> Unit,
    onAccountClicked: () -> Unit,
    onDiariesClicked: () -> Unit,
    onGroupClicked: () -> Unit,
){

    NavigationBar() {
        NavigationBarItem(
            selected = false,
            onClick = onHomeClicked,
            icon = {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "home icon"
                    )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick =onGroupClicked,
            icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.empathy),
                        contentDescription = "share icon"
                    )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick =onDiariesClicked,
            icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.diaries),
                        contentDescription = "Person icon"
                    )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick =onAccountClicked,
            icon = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Person icon"
                    )
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun NavigationBarPreview(){
    DiaryAppTheme {
        DiaryNavigation({},{},{},{})
    }
}