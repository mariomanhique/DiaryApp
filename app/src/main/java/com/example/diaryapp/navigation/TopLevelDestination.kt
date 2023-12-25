package com.example.diaryapp.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.diaryapp.icons.DiaryIcons
import com.example.diaryapp.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int
) {
    HOME(
        selectedIcon = DiaryIcons.Home,
        unselectedIcon = DiaryIcons.HomeBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home
    ),
    PROFILE(
        selectedIcon = DiaryIcons.Profile,
        unselectedIcon = DiaryIcons.ProfileBorder,
        iconTextId = R.string.profile,
        titleTextId = R.string.profile
    ),



}