package com.kito.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class SecureStorage {
    actual suspend fun saveSapPassword(password: String): Boolean {
        TODO("iOS Keychain implementation")
    }

    actual suspend fun getSapPassword(): String {
        TODO("iOS Keychain implementation")
    }

    actual val isLoggedInFlow: Flow<Boolean> = flowOf(false)

    actual suspend fun clearSapPassword(): Boolean {
        TODO("iOS Keychain implementation")
    }
}
