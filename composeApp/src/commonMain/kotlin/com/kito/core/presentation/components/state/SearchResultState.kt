package com.kito.core.presentation.components.state

sealed class SearchResultState {
    object Success : SearchResultState()
    object Empty : SearchResultState()
    object Idle: SearchResultState()
}

