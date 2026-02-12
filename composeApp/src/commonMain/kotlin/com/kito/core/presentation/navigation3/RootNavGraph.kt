package com.kito.core.presentation.navigation3

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kito.feature.exam.presentation.UpcomingExamScreen
import com.kito.feature.faculty.presentation.FacultyDetailScreen
import com.kito.feature.schedule.presentation.ScheduleScreen

@Composable
fun RootNavGraph(
    rootNavBackStack: NavBackStack<NavKey>,
    tabNavBackStack: NavBackStack<NavKey>,
    snackbarHostState: SnackbarHostState,
){
    NavDisplay(
        backStack = rootNavBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        entryProvider = entryProvider{
            entry<Routes.Tabs>{
                TabNavGraph(
                    rootNavStack = rootNavBackStack,
                    tabNavStack = tabNavBackStack,
                    snackbarHostState = snackbarHostState
                )
            }
            entry<Routes.Schedule>{
                ScheduleScreen()
            }
            entry<Routes.ExamSchedule>{
                UpcomingExamScreen()
            }
            entry<Routes.FacultyDetail>{
                FacultyDetailScreen(
                    facultyId = it.facultyId
                )
            }
        }
    )
}