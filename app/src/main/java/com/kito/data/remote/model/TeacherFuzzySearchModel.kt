package com.kito.data.remote.model

import androidx.annotation.Keep

@Keep
data class TeacherFuzzySearchModel(
    val email: String? = null,
    val name: String? = null,
    val office_room: String? = null,
    val score: Double? = null,
    val teacher_id: Long? = null
)