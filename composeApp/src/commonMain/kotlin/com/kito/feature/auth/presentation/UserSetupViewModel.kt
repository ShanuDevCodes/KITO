package com.kito.feature.auth.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.datastore.PrefsRepository
import com.kito.core.platform.SecureStorage
import com.kito.core.presentation.components.AppSyncUseCase
class UserSetupViewModel(
    private val prefs: PrefsRepository,
    private val secureStorage: SecureStorage,
    private val appSyncUseCase: AppSyncUseCase
) : ViewModel(){
    private val _setupState = MutableStateFlow<SetupState>(SetupState.Idle)
    val setupState = _setupState.asStateFlow()
    suspend fun setUserName(name: String) {
        val formattedName = name
            .trim()
            .replace("\\s+".toRegex(), " ")
            .lowercase()
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercaseChar() }
            }

        prefs.setUserName(formattedName)
    }
    suspend fun setUserRoll(roll: String){
        prefs.setUserRollNumber(roll)
    }
    suspend fun setSapPassword(sapPassword: String){
        secureStorage.saveSapPassword(sapPassword)
    }
    suspend fun setUserSetupDone() {
        prefs.setUserSetupDone()
    }
    suspend fun setAcademicYear(year: String) {
        prefs.setAcademicYear(year)
    }
    suspend fun setTermCode(term: String) {
        prefs.setTermCode(term)
    }

    fun completeSetup(
        name: String,
        roll: String,
        sapPassword: String,
        year: String = "2025",
        term: String = "020"
    ) {
        viewModelScope.launch {
            _setupState.value = SetupState.Loading
            try {
                setUserName(name)
                setUserRoll(roll)
                setAcademicYear(year)
                setTermCode(term)
                val result = appSyncUseCase.syncAll(
                    roll = roll,
                    sapPassword = sapPassword,
                    year = year,
                    term = term
                )

                result.fold(
                    onSuccess = {
                        if (sapPassword.isNotEmpty()) {
                            setSapPassword(sapPassword)
                        }
                        setUserSetupDone()
                        _setupState.value = SetupState.Success
                    },
                    onFailure = {
                        _setupState.value = SetupState.Error(
                            it.message ?: "Sync failed"
                        )
                    }
                )
            } catch (e: Exception) {
                _setupState.value = SetupState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

}

sealed class SyncResult {
    object Success : SyncResult()
    data class Error(val message: String) : SyncResult()
}
sealed class SetupState {
    object Idle : SetupState()
    object Loading : SetupState()
    object Success : SetupState()
    data class Error(val message: String) : SetupState()
}



