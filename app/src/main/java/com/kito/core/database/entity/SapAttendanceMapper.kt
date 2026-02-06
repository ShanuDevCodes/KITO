package com.kito.core.database.entity

import com.kito.sap.SubjectAttendance

/**
 * Extension function to convert SubjectAttendance to AttendanceEntity
 */
fun SubjectAttendance.toAttendanceEntity(year: String, term: String): AttendanceEntity {
    return AttendanceEntity(
        subjectCode = this.subjectCode,
        subjectName = this.subjectName,
        attendedClasses = this.attendedClasses,
        totalClasses = this.totalClasses,
        percentage = this.percentage,
        facultyName = this.facultyName,
        year = year,
        term = term
    )
}
