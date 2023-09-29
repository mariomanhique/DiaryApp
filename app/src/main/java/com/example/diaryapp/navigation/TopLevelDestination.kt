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
    MEMORIES(
    selectedIcon = DiaryIcons.memories,
    unselectedIcon = DiaryIcons.memoriesBorder,
    iconTextId = R.string.memories,
    titleTextId =R.string.memories
    ),
    SETTINGS(
        selectedIcon = DiaryIcons.Settings,
        unselectedIcon = DiaryIcons.SettingsBorder,
        iconTextId = R.string.settings,
        titleTextId = R.string.settings
    )



}