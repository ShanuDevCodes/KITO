package com.kito.data.remote.model

import androidx.annotation.Keep

@Keep
data class TeacherScheduleByIDModel(
    val batch: String? = null,
    val day: String? = null,
    val end_time: String? = null,
    val room: String? = null,
    val start_time: String? = null,
    val subject: String? = null,
    val teacher: String? = null,
    val week_type: Int? = null
)