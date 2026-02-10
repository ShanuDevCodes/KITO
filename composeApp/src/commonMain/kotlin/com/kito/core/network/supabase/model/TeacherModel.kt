package com.kito.core.network.supabase.model

import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Serializable
@Keep
data class TeacherModel(
    val email: String? = null,
    val name: String? = null,
    val office_room: String? = null,
    val teacher_id: Long? = null
)
