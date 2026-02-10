package com.kito.feature.schedule.notification

import com.kito.core.datastore.StudentSectionDatastore
import kotlinx.datetime.*

fun computeNextClass(
    sections: List<StudentSectionDatastore>
): Pair<StudentSectionDatastore?, Long?> {
    val now = Clock.System.now().toEpochMilliseconds()
    val next = sections
        .map { it to classStartMillis(it) }
        .filter { (_, startMillis) -> startMillis > now }
        .minByOrNull { it.second }
    return next?.let { (entity, millis) ->
        entity to millis
    } ?: (null to null)
}

fun classStartMillis(section: StudentSectionDatastore): Long {
    val now = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(timeZone).date

    val (hour, minute) = section.startTime
        .split(":")
        .take(2)
        .map { it.trim().toInt() }

    val classTime = LocalTime(hour, minute)

    val targetDayOfWeek = when (section.day) {
        "MON" -> DayOfWeek.MONDAY
        "TUE" -> DayOfWeek.TUESDAY
        "WED" -> DayOfWeek.WEDNESDAY
        "THU" -> DayOfWeek.THURSDAY
        "FRI" -> DayOfWeek.FRIDAY
        "SAT" -> DayOfWeek.SATURDAY
        else -> DayOfWeek.SUNDAY
    }

    // Calculate days until target day (0-6)
    val daysDiff = (targetDayOfWeek.ordinal - today.dayOfWeek.ordinal + 7) % 7
    var targetDate = today.plus(daysDiff, DateTimeUnit.DAY)
    
    // If today is the day, check if time has passed
    if (daysDiff == 0) {
         val nowTime = now.toLocalDateTime(timeZone).time
         if (classTime <= nowTime) {
             targetDate = targetDate.plus(7, DateTimeUnit.DAY)
         }
    }

    return targetDate.atTime(classTime).toInstant(timeZone).toEpochMilliseconds()
}

