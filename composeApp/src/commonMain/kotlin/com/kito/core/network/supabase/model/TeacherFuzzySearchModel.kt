package com.kito.core.network.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherFuzzySearchModel(
    val email: String? = null,
    val name: String? = null,
    val office_room: String? = null,
    val score: Double? = null,
    val teacher_id: Long? = null
)
