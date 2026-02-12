package com.kito.core.presentation.navigation3

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kito.feature.attendance.presentation.AttendanceListScreen
import com.kito.feature.faculty.presentation.FacultyScreen
import com.kito.feature.home.presentation.HomeScreen
import com.kito.feature.settings.presentation.SettingsScreen

@Composable
fun TabNavGraph(
    rootNavStack: NavBackStack<NavKey>,
    tabNavStack: NavBackStack<NavKey>,
    snackbarHostState: SnackbarHostState
){
    NavDisplay(
        backStack = tabNavStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<TabRoutes.Home> {
                HomeScreen(
                    rootNavBackStack = rootNavStack,
                    tabNavBackStack = tabNavStack
                )
            }
            entry<TabRoutes.Profile> {
                SettingsScreen(
                    tabNavBackStack = tabNavStack,
                    snackbarHostState = snackbarHostState
                )
            }
            entry<TabRoutes.Faculty> {
                FacultyScreen(
                    rootNavBackStack = rootNavStack
                )
            }
            entry<TabRoutes.Attendance> {
                AttendanceListScreen()
            }
        }
    )
}