package com.kito.core.network.supabase.request

import kotlinx.serialization.Serializable

@Serializable
data class MidsemScheduleRequest(
    val p_roll_no: String
)
