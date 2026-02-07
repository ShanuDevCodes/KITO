package com.kito.core.database.repository

import com.kito.core.database.dao.StudentSectionDAO
import com.kito.core.database.entity.StudentSectionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentSectionRepository @Inject constructor(
    private val studentSectionDao: StudentSectionDAO
) {
    fun getScheduleForStudent(rollNo: String, day: String): Flow<List<StudentSectionEntity>> =
        studentSectionDao.getScheduleForStudent(rollNo, day)

    fun getAllScheduleForStudent(rollNo: String): Flow<List<StudentSectionEntity>> =
        studentSectionDao.getAllScheduleForStudent(rollNo)
}
