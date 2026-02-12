package com.kito.core.database.repository

import com.kito.core.database.dao.StudentSectionDAO
import com.kito.core.database.entity.StudentSectionEntity
import kotlinx.coroutines.flow.Flow
class StudentSectionRepository(
    private val studentSectionDao: StudentSectionDAO
) {
    fun getScheduleForStudent(rollNo: String, day: String): Flow<List<StudentSectionEntity>> =
        studentSectionDao.getScheduleForStudent(rollNo, day)

    fun getAllScheduleForStudent(rollNo: String): Flow<List<StudentSectionEntity>> =
        studentSectionDao.getAllScheduleForStudent(rollNo)
}
