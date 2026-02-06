package com.kito.core.presentation.components

import android.content.Context
import com.kito.core.datastore.ProtoDatastoreRepository
import com.kito.core.datastore.StudentSectionDatastore
import com.kito.core.database.repository.AttendanceRepository
import com.kito.core.database.entity.toAttendanceEntity
import com.kito.core.database.repository.SectionRepository
import com.kito.core.database.repository.StudentRepository
import com.kito.core.database.repository.StudentSectionRepository
import com.kito.core.network.supabase.SupabaseRepository
import com.kito.feature.schedule.notification.NotificationPipelineController
import com.kito.sap.AttendanceResult
import com.kito.sap.SapRepository
import com.kito.feature.schedule.widget.WidgetUpdater.nudgeRedraw
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class AppSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseRepository: SupabaseRepository,
    private val studentRepository: StudentRepository,
    private val sectionRepository: SectionRepository,
    private val studentSectionRepository: StudentSectionRepository,
    private val attendanceRepository: AttendanceRepository,
    private val sapRepository: SapRepository,
    private val protoRepo: ProtoDatastoreRepository
) {

    suspend fun syncAll(
        roll: String,
        sapPassword: String,
        year: String,
        term: String
    ): Result<Unit> = supervisorScope {
        try {
            val student = supabaseRepository.getStudentByRoll(roll)
            val timetable = supabaseRepository.getTimetableForStudent(
                section = student.section,
                batch = student.batch
            )
            coroutineScope {
                async { studentRepository.insertStudent(listOf(student)) }
                async { sectionRepository.insertSection(timetable) }
            }
            if (sapPassword.isNotEmpty()) {
                when (
                    val response = sapRepository.login(
                        username = roll,
                        password = sapPassword,
                        academicYear = year,
                        termCode = term
                    )
                ) {
                    is AttendanceResult.Success -> {
                        attendanceRepository.insertAttendance(
                            response.data.subjects.map {
                                it.toAttendanceEntity(year, term)
                            }
                        )
                    }
                    is AttendanceResult.Error -> {
                        throw IllegalStateException(response.message)
                    }
                }
            }
            val sections =
                studentSectionRepository.getAllScheduleForStudent(rollNo = roll).first()
            val protoList = sections.map {
                StudentSectionDatastore(
                    sectionId = it.sectionId,
                    rollNo = it.rollNo,
                    section = it.section,
                    batch = it.batch,
                    day = it.day,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    subject = it.subject,
                    room = it.room
                )
            }.toPersistentList()
            protoRepo.setSections(protoList)
            protoRepo.setRollNo(roll)
            nudgeRedraw(context)
            NotificationPipelineController.get(context).sync()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

