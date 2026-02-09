package com.kito.feature.app.presentation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.database.repository.SectionRepository
import com.kito.core.datastore.PrefsRepository
class AppViewModel(
    private val pref: PrefsRepository,
    private val sectionRepository: SectionRepository
): ViewModel() {
    fun checkResetFix(){
        viewModelScope.launch {
            val isResetFixDone = pref.resetFixFlow.first()
            if(!isResetFixDone){
                sectionRepository.deleteAllSection()
                pref.setResetDone()
            }
        }
    }
}


