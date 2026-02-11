//package com.kito.core.presentation.navigation
//
//fun slideTransition() = NavDisplay.transitionSpec {
//    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
//} + NavDisplay.popTransitionSpec {
//    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
//} + NavDisplay.predictivePopTransitionSpec {
//    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
//}