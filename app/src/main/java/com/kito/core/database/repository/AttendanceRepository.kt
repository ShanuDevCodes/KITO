package com.kito.core.database.repository

import com.kito.core.database.dao.AttendanceDAO
import com.kito.core.database.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDAO
) {
    suspend fun insertAttendance(attendance: List<AttendanceEntity>) =
        attendanceDao.insertAttendance(attendance)

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity) =
        attendanceDao.deleteAttendance(attendanceEntity)

    fun getAllAttendance(): Flow<List<AttendanceEntity>> =
        attendanceDao.getAllAttendance()

    suspend fun deleteAllAttendance() =
        attendanceDao.deleteAllAttendance()
}
