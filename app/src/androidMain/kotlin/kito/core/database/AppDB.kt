package com.kito.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kito.core.database.dao.AttendanceDAO
import com.kito.core.database.dao.SectionDAO
import com.kito.core.database.dao.StudentDAO
import com.kito.core.database.dao.StudentSectionDAO
import com.kito.core.database.entity.AttendanceEntity
import com.kito.core.database.entity.SectionEntity
import com.kito.core.database.entity.StudentEntity

@Database(
    entities = [AttendanceEntity::class, StudentEntity::class, SectionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDB: RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDAO
    abstract fun studentDao(): StudentDAO
    abstract fun sectionDao(): SectionDAO
    abstract fun studentSectionDao(): StudentSectionDAO
}
