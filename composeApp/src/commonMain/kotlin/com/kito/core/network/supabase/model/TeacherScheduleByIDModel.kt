package com.kito.core.network.supabase.model

import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Serializable
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
