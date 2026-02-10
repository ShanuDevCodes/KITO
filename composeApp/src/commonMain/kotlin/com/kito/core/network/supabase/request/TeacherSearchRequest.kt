package com.kito.core.network.supabase.request

import kotlinx.serialization.Serializable

@Serializable
data class TeacherSearchRequest(
    val p_query: String="",
    val p_threshold: Double = 0.4
)
