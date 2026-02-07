package com.kito.core.database.repository

import com.kito.core.database.dao.StudentDAO
import com.kito.core.database.entity.StudentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val studentDao: StudentDAO
) {
    suspend fun insertStudent(studentEntity: List<StudentEntity>) =
        studentDao.insertStudent(studentEntity)

    suspend fun deleteStudent(studentEntity: StudentEntity) =
        studentDao.deleteStudent(studentEntity)

    suspend fun getStudentByRoll(rollNo: String): StudentEntity =
        studentDao.getStudentByRoll(rollNo)
}
