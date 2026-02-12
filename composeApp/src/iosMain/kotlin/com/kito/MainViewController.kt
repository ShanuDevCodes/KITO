package com.kito

import androidx.compose.ui.window.ComposeUIViewController
import com.kito.feature.app.presentation.MainUI

import com.kito.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    MainUI()
}
