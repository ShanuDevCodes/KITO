package com.kito

import androidx.compose.ui.window.ComposeUIViewController
import com.kito.core.di.initKoin
import com.kito.feature.app.presentation.MainUI

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    MainUI()
}
