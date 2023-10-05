package com.example.diaryapp.presentation

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diaryapp.R
import com.mariomanhique.ui.theme.DiaryAppTheme


val list = listOf(
    NavItem(
        R.drawable.k_home,
        "Home"
    ),
    NavItem(
        R.drawable.k_memory,
        "Memories"
    ),
//    NavItem(
//        R.drawable.k_search,
//        "Search"
//    ),
    NavItem(
        R.drawable.k_settings,
        "Settings"
    ),
)

val list2 = listOf(
    NavItem(
        R.drawable.k_home,
        "Home"
    ),
    NavItem(
        R.drawable.k_memory,
        "Memories"
    ),
//    NavItem(
//        R.drawable.k_search,
//        "Search"
//    ),
    NavItem(
        R.drawable.k_settings,
        "Settings"
    ),
)

data class NavItem(
    @DrawableRes val icon:Int,
    val title: String
)



@Preview(
    backgroundColor = 0xFFC4C2C4,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun Display(){
    Column(modifier = Modifier.wrapContentHeight()) {
        Nav1(0,list = list)
//        Spacer(modifier = Modifier.height(12.dp))
//        Nav2(0,list = list2)

    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
    )
@Composable
fun NavPreview(){
    DiaryAppTheme{
        Nav2(0, list = list2)
    }
}

@Composable
fun Nav2(
    defaultSelectedIndex: Int=0,
    list: List<NavItem>
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
        )
    ){

        var selectedIndex by remember {
            mutableStateOf(defaultSelectedIndex)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            list.forEachIndexed { index, navItem ->

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(11f)
                        .clickable {
                            selectedIndex = index
                        },
                    contentAlignment = Alignment.Center
                    ){

                    Column(
                        if(selectedIndex == index) Modifier.offset(y = (-8).dp)
                        else Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                       Box(
                           modifier = Modifier
                               .background(
                                   if (selectedIndex == index) MaterialTheme.colorScheme.primary
                                   else Color.Transparent,
                                   shape = CircleShape
                               )
                               .size(36.dp),
                           contentAlignment = Alignment.Center
                       ) {
                           Icon(painter = painterResource(
                               id = navItem.icon),
                               contentDescription = null,
                               modifier = Modifier.size(24.dp),
                               tint = if(selectedIndex == index) Color.White else Color.Gray
                           )
                       }

                        AnimatedVisibility(visible = selectedIndex == index) {
                            Text(
                                text = navItem.title,
                                modifier = Modifier.padding(top = 4.dp),
//                                color = Color.DarkGray,
                                fontSize = 12.sp
                                )
                        }

                    }
                }

            }
        }
//        Text(text = "Text")
    }
}
@Composable
fun Nav1(
    defaultSelectedIndex: Int=0,
    list: List<NavItem>
){

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surface
    ) {

        var selectedIndex by remember {

            mutableStateOf(defaultSelectedIndex)

        }

        Row(modifier = Modifier.fillMaxSize()){
            list.forEachIndexed { index, navItem ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                    contentAlignment = Alignment.Center
                ){

                    Box(modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            selectedIndex = index
                        }
                        .background(
                            color = if (selectedIndex == index) MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = RoundedCornerShape(4.dp),
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
//                            if(selectedIndex == index) Modifier.offset(y = (-8).dp)
//                            else Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (selectedIndex == index) MaterialTheme.colorScheme.primary
                                        else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(painter = painterResource(
                                    id = navItem.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = if(selectedIndex == index) Color.White else Color.Gray
                                )
                            }

                            AnimatedVisibility(visible = selectedIndex == index) {
                                Text(
                                    text = navItem.title,
                                    modifier = Modifier.padding(top = 4.dp),
//                                color = Color.DarkGray,
                                    fontSize = 12.sp
                                )
                            }

                        }
                    }

                }
            }
        }
    }

}