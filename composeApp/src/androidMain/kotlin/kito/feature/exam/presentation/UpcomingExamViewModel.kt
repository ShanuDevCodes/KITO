package com.kito.feature.exam.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.datastore.PrefsRepository
import com.kito.core.network.supabase.SupabaseRepository
import com.kito.core.network.supabase.model.MidsemScheduleModel
import com.kito.core.presentation.components.state.SyncUiState
@RequiresApi(Build.VERSION_CODES.O)
class UpcomingExamViewModel(
    private val prefs: PrefsRepository,
    private val supabaseRepository: SupabaseRepository
): ViewModel() {

    private val _examModel = MutableStateFlow<List<MidsemScheduleModel>>(emptyList())
    val examModel = _examModel.asStateFlow()
    private val _uiState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        getExamSchedule()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExamSchedule(){
        viewModelScope.launch {
            _uiState.value = SyncUiState.Loading
            try {
                val roll = prefs.userRollFlow.first()
                _examModel.value = getUpcomingOrOngoingExams(supabaseRepository.getMidSemSchedule(roll))
                _uiState.value = SyncUiState.Success
            }catch (e: Exception){
                Log.d("exam model error",e.message?:"")
                _uiState.value = SyncUiState.Error(e.message?:"")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getUpcomingOrOngoingExams(
        exams: List<MidsemScheduleModel>
    ): List<MidsemScheduleModel> {

        val nowDate = LocalDate.now()
        val nowTime = LocalTime.now()

        return exams
            .mapNotNull { exam ->
                try {
                    val examDate = LocalDate.parse(exam.date)
                    val startTime = LocalTime.parse(exam.start_time)
                    val endTime = LocalTime.parse(exam.end_time)

                    when {
                        // 🟢 ONGOING exam
                        examDate == nowDate &&
                                !nowTime.isBefore(startTime) &&
                                nowTime.isBefore(endTime) -> {
                            exam to LocalDateTime.of(examDate, startTime)
                        }
                        examDate.isAfter(nowDate) ||
                                (examDate == nowDate && startTime.isAfter(nowTime)) -> {
                            exam to LocalDateTime.of(examDate, startTime)
                        }
                        else -> null
                    }
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.second }
            .map { it.first }
    }
}



