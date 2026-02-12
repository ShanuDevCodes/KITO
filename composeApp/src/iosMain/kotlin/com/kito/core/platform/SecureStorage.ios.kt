package com.kito.core.platform

import com.kito.core.datastore.PrefsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class SecureStorage : KoinComponent {
    
    private val prefsRepository: PrefsRepository by inject()

    actual val isLoggedInFlow: Flow<Boolean> = prefsRepository.sapPasswordFlow
        .map { it.isNotEmpty() }

    actual suspend fun saveSapPassword(password: String): Boolean {
        prefsRepository.saveSapPassword(password)
        return true
    }

    actual suspend fun getSapPassword(): String {
        return prefsRepository.getSapPassword()
    }

    actual suspend fun clearSapPassword(): Boolean {
        prefsRepository.clearSapPassword()
        return true
    }
}


