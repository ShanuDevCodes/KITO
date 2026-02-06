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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

    @GET("rest/v1/timetable")
    suspend fun getTimetable(): List<SectionEntity>

    @GET("rest/v1/students")
    suspend fun getStudents(): List<StudentEntity>

    @GET("rest/v1/students")
    suspend fun getStudentByRoll(
        @Query("roll_no") rollFilter: String,
        @Query("select") select: String = "*"
    ): List<StudentEntity>

    @GET("rest/v1/timetable")
    suspend fun getTimetableForStudent(
        @Query("section") sectionFilter: String,
        @Query("batch") batchFilter: String,
        @Query("select") select: String = "*"
    ): List<SectionEntity>

    @GET("rest/v1/v_teachers_with_details")
    suspend fun getAllTeacherDetail(): List<TeacherModel>

    @POST("rest/v1/rpc/search_teachers_fuzzy")
    suspend fun getTeacherSearchResponse(
        @Body request : TeacherSearchRequest
    ):List<TeacherFuzzySearchModel>

    @POST("rest/v1/rpc/get_teacher_schedule_by_id")
    suspend fun getTeacherScheduleById(
        @Body request: TeacherScheduleByIDRequest
    ): List<TeacherScheduleByIDModel>

    @GET("rest/v1/v_teachers_with_details")
    suspend fun getTeacherDetailByID(
        @Query("teacher_id") teacherId: String
    ): List<TeacherModel>

    @POST("rest/v1/rpc/get_midsem_schedule_by_roll")
    suspend fun getMidSemSchedule(
        @Body request: MidsemScheduleRequest
    ): List<MidsemScheduleModel>

}
