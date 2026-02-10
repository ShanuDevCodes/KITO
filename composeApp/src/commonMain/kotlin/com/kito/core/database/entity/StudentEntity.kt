package com.kito.core.database.entity

import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Serializable
@Entity
data class StudentEntity (
    @PrimaryKey
    val roll_no: String = "",
    val section: String = "",
    val batch: String = ""
)
