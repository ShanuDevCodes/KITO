package com.kito.core.network.supabase.request

import kotlinx.serialization.Serializable

@Serializable
data class TeacherScheduleByIDRequest(
    val p_teacher_id: Long=0
)
