package com.kito.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(
    val title: String,
    val icon: ImageVector,
    val color: Color
){
    data object Home : BottomBarTab(
        title = "Home",
        icon = Icons.Filled.Home,
        color = Color(0xFFFFA574)
    )
    data object Attendance : BottomBarTab(
        title = "Attendance",
        icon = Icons.Filled.CheckCircle,
        color = Color(0xFFFFA574)
    )
    data object Faculty : BottomBarTab(
        title = "Faculty",
        Icons.Default.School,
        Color(0xFFFFA574)
    )
    data object Settings : BottomBarTab(
        title = "Settings",
        icon = Icons.Filled.Settings,
        color = Color(0xFFFFA574)
    )
}

val tabs = listOf(
    BottomBarTab.Home,
    BottomBarTab.Attendance,
    BottomBarTab.Faculty,
    BottomBarTab.Settings
)
