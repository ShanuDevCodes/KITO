package com.kito.core.network.supabase

import com.kito.core.database.entity.SectionEntity
import com.kito.core.database.entity.StudentEntity
import com.kito.core.network.supabase.model.MidsemScheduleModel
import com.kito.core.network.supabase.model.TeacherFuzzySearchModel
import com.kito.core.network.supabase.model.TeacherModel
import com.kito.core.network.supabase.model.TeacherScheduleByIDModel
import com.kito.core.network.supabase.request.MidsemScheduleRequest
import com.kito.core.network.supabase.request.TeacherScheduleByIDRequest
import com.kito.core.network.supabase.request.TeacherSearchRequest
class SupabaseRepository(
    private val api: SupabaseApi
) {

    suspend fun getStudents(): List<StudentEntity> {
        return api.getStudents()
    }

    suspend fun getSection(): List<SectionEntity> {
        return api.getTimetable()
    }

    suspend fun getStudentByRoll(rollNo: String): StudentEntity {
        val result = api.getStudentByRoll("eq.$rollNo")

        if (result.isEmpty()) {
            throw IllegalStateException("Student not found in Supabase")
        }

        return result.first()
    }

    suspend fun getTimetableForStudent(
        section: String,
        batch: String
    ): List<SectionEntity> {
        return api.getTimetableForStudent(
            sectionFilter = "eq.$section",
            batchFilter = "eq.$batch"
        )
    }

    suspend fun getAllTeacherDetail(): List<TeacherModel> {
        return api.getAllTeacherDetail()
    }

    suspend fun getTeacherSearchResponse(query: String): List<TeacherFuzzySearchModel> {
        return api.getTeacherSearchResponse(
            request = TeacherSearchRequest(
                p_query = query
            )
        )
    }

    suspend fun getTeacherScheduleById(teacherId: Long): List<TeacherScheduleByIDModel> {
        return api.getTeacherScheduleById(
            request = TeacherScheduleByIDRequest(
                p_teacher_id = teacherId
            )
        )
    }

    suspend fun getTeacherDetailByID(teacherId: Long): List<TeacherModel>{
        return api.getTeacherDetailByID(
            teacherId = "eq.$teacherId"
        )
    }

    suspend fun getMidSemSchedule(rollNo: String): List<MidsemScheduleModel> {
        return api.getMidSemSchedule(
            request = MidsemScheduleRequest(
                p_roll_no = rollNo
            )
        )
    }
}
