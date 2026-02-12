package com.kito.feature.exam.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.common.util.currentLocalDateTime
import com.kito.core.datastore.PrefsRepository
import com.kito.core.network.supabase.SupabaseRepository
import com.kito.core.network.supabase.model.MidsemScheduleModel
import com.kito.core.presentation.components.state.SyncUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

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

    fun getExamSchedule(){
        viewModelScope.launch {
            _uiState.value = SyncUiState.Loading
            try {
                val roll = prefs.userRollFlow.first()
                _examModel.value = getUpcomingOrOngoingExams(supabaseRepository.getMidSemSchedule(roll))
                _uiState.value = SyncUiState.Success
            }catch (e: Exception){
                println("exam model error: ${e.message}")
                _uiState.value = SyncUiState.Error(e.message?:"")
            }
        }
    }

    fun getUpcomingOrOngoingExams(
        exams: List<MidsemScheduleModel>
    ): List<MidsemScheduleModel> {

        val now = currentLocalDateTime()
        val nowDate = now.date
        val nowTime = now.time

        return exams
            .mapNotNull { exam ->
                try {
                    val examDate = LocalDate.parse(exam.date)
                    val startTime = LocalTime.parse(exam.start_time)
                    val endTime = LocalTime.parse(exam.end_time)

                    when {
                        // 🟢 ONGOING exam
                        examDate == nowDate &&
                                nowTime >= startTime &&
                                nowTime < endTime -> {
                            exam to LocalDateTime(examDate, startTime)
                        }
                        examDate > nowDate ||
                                (examDate == nowDate && startTime > nowTime) -> {
                            exam to LocalDateTime(examDate, startTime)
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



