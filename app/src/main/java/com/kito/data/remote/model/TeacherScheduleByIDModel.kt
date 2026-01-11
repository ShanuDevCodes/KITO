package com.kito.data.remote.model

data class TeacherScheduleByIDModel(
    val batch: String,
    val day: String,
    val end_time: String,
    val room: String,
    val start_time: String,
    val subject: String,
    val teacher: String,
    val week_type: Int
)