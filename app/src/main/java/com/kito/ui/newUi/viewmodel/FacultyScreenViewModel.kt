package com.kito.ui.newUi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.data.remote.SupabaseRepository
import com.kito.data.remote.model.TeacherFuzzySearchModel
import com.kito.data.remote.model.TeacherModel
import com.kito.ui.components.state.SearchResultState
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
    private val _searchResultState = MutableStateFlow<SearchResultState>(SearchResultState.Idle)
    val searchResultState = _searchResultState.asStateFlow()
    private val _facultySearchResult = MutableStateFlow<List<TeacherFuzzySearchModel>>(emptyList())
    val facultySearchResult = _facultySearchResult.asStateFlow()

    init {
        viewModelScope.launch {
            _faculty.value = repository.getAllTeacherDetail()
        }
    }
    fun getSearchResult(query: String){
        viewModelScope.launch {
            if (query.isEmpty()){
                _facultySearchResult.value = emptyList()
                _searchResultState.value = SearchResultState.Idle
            }else {
                _facultySearchResult.value = repository.getTeacherSearchResponse(query)
                if (_facultySearchResult.value.isEmpty()){
                    _searchResultState.value = SearchResultState.Empty
                }else {
                    _searchResultState.value = SearchResultState.Success
                }
            }
        }
    }

    fun clearSearchResult(){
        _facultySearchResult.value = emptyList()
        _searchResultState.value = SearchResultState.Idle
    }
}