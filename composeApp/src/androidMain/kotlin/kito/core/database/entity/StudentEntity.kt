package com.kito.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudentEntity (
    @PrimaryKey
    val roll_no: String = "",
    val section: String = "",
    val batch: String = ""
)
