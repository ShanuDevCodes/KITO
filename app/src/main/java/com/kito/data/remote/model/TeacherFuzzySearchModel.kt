package com.kito.data.remote.model

data class TeacherFuzzySearchModel(
    val email: String,
    val name: String,
    val office_room: String,
    val score: Double,
    val teacher_id: Long
)