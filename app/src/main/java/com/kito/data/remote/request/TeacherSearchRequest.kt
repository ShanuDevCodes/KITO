package com.kito.data.remote.request

data class TeacherSearchRequest(
    val p_query: String,
    val p_threshold: Double = 0.4
)