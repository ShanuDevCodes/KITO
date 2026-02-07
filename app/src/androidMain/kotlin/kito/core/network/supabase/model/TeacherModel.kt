package com.kito.core.network.supabase.model

import androidx.annotation.Keep

@Keep
data class TeacherModel(
    val email: String? = null,
    val name: String? = null,
    val office_room: String? = null,
    val teacher_id: Long? = null
)
