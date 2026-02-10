package com.kito.core.database.entity

import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Serializable
@Entity
data class SectionEntity(
    @PrimaryKey
    val id: Int = 0,
    val section: String = "",
    val day: String = "",
    val start_time: String = "",
    val end_time: String = "",
    val subject: String = "",
    val room: String? = "",
    val batch: String = "",
)
