package com.kito.ui.newUi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.data.remote.SupabaseRepository
import com.kito.data.remote.model.TeacherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultyScreenViewModel @Inject constructor(
    private val repository : SupabaseRepository
) : ViewModel() {
    private val _faculty = MutableStateFlow<List<TeacherModel>>(emptyList())
    val faculty = _faculty.asStateFlow()

    init {
        viewModelScope.launch {
            _faculty.value = repository.getAllTeacherDetail()
        }
    }
}