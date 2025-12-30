package com.kito.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kito.ScheduleActivity
import com.kito.data.local.db.studentsection.StudentSectionEntity
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import java.util.Calendar

class TimetableWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        Log.d("Widget", "provideGlance called")
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetEntryPoint::class.java
        )

        val prefs = entryPoint.prefsRepository()
        val repo = entryPoint.studentSectionRepository()

        val rollNo = prefs.userRollFlow.first()

        val day = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "MON"
            Calendar.TUESDAY -> "TUE"
            Calendar.WEDNESDAY -> "WED"
            Calendar.THURSDAY -> "THU"
            Calendar.FRIDAY -> "FRI"
            Calendar.SATURDAY -> "SAT"
            else -> "SUN"
        }

        val schedule = if (rollNo.isNotEmpty()) {
            repo.getScheduleForStudentBlocking(rollNo, day)
        } else {
            emptyList()
        }
//        val schedule = sampleSchedule

        provideContent {
            Log.d(
                "WidgetWorker",
                "Widget content updated"
            )
            TimetableWidgetContent(
                rollNo = rollNo,
                day = day,
                schedule = schedule
            )
        }
    }
    @Composable
    private fun TimetableWidgetContent(
        rollNo: String,
        day: String,
        schedule: List<StudentSectionEntity>
    ) {
        val bgTop = Color(0xFF1A1423)
        val cardBg = Color(0xFF261E26)
        val textPrimaryK = Color(0xFFF3EFF3)
        val textSecondaryK = Color(0xFFCECAD0)
        val accentOrange = Color(0xFFEA850A)
        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(bgTop)
                    .cornerRadius(24.dp)
                    .padding(12.dp)
                    .clickable(actionStartActivity<ScheduleActivity>()) // Redirect to schedule screen
            ) {
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = GlanceModifier.defaultWeight()) {
                            Text(
                                text = day,
                                style = TextStyle(
                                    color = ColorProvider(day = accentOrange, night = accentOrange),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    Spacer(modifier = GlanceModifier.height(12.dp))

                    when {
                        rollNo.isEmpty() -> {
                            Box(
                                modifier = GlanceModifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Please login in the app",
                                    style = TextStyle(
                                        color = ColorProvider(day = textSecondaryK, night = textSecondaryK),
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }

                        schedule.isEmpty() -> {
                            Box(
                                modifier = GlanceModifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (day == "SUN") "Happy Sunday! 🏖️" else "No classes today",
                                    style = TextStyle(
                                        color = ColorProvider(day = textSecondaryK, night = textSecondaryK),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        else -> {
                            LazyColumn(modifier = GlanceModifier.fillMaxWidth()) {
                                items(schedule) { item ->
                                    TimetableItem(
                                        item = item,
                                        cardBg = cardBg,
                                        textPrimary = textPrimaryK,
                                        textSecondary = textSecondaryK,
                                        accent = accentOrange
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TimetableItem(
        item: StudentSectionEntity,
        cardBg: Color,
        textPrimary: Color,
        textSecondary: Color,
        accent: Color
    ) {
        val startTime = formatTime(item.startTime)
        val endTime = formatTime(item.endTime)
        Column(
            modifier = GlanceModifier.padding(bottom = 8.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .clickable(actionStartActivity<ScheduleActivity>())
                    .background(cardBg)
                    .cornerRadius(16.dp)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = GlanceModifier.width(60.dp)) {
                    Text(
                        text = startTime,
                        style = TextStyle(
                            color = ColorProvider(day = textPrimary, night = textPrimary),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = endTime,
                        style = TextStyle(
                            color = ColorProvider(day = textSecondary, night = textSecondary),
                            fontSize = 10.sp
                        )
                    )
                }

                Box(
                    modifier = GlanceModifier
                        .width(2.dp)
                        .height(30.dp)
                        .background(accent)
                ) {}

                Spacer(modifier = GlanceModifier.width(8.dp))

                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = item.subject,
                        style = TextStyle(
                            color = ColorProvider(day = textPrimary, night = textPrimary),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                    if (item.room != null) {
                        Text(
                            text = "Room: ${item.room}",
                            style = TextStyle(
                                color = ColorProvider(day = textSecondary, night = textSecondary),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }

    private fun formatTime(timeStr: String): String {
        return try {
            val parts = timeStr.split(":")
            if (parts.size >= 2) {
                "${parts[0].trim()}:${parts[1].trim().take(2)}"
            } else {
                timeStr
            }
        } catch (e: Exception) {
            timeStr
        }
    }

    private fun timeToMinutes(timeStr: String): Int {
        return try {
            val parts = timeStr.split(":")
            parts[0].trim().toInt() * 60 + parts[1].trim().take(2).toInt()
        } catch (e: Exception) {
            0
        }
    }
}