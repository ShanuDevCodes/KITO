package com.kito.core.presentation.components

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartupSyncGuard @Inject constructor(){
    var hasSynced: Boolean = false
}

