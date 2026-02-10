package com.kito.core.common.util

import kotlinx.datetime.LocalDate

/**
 * Converts 24-hour time string (HH:mm:ss) to 12-hour format (hh:mm AM/PM).
 * Pure Kotlin implementation — no java.text.SimpleDateFormat.
 */
fun formatTo12Hour(time: String): String {
    return try {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val period = if (hour < 12) "AM" else "PM"
        val adjustedHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        "${"$adjustedHour".padStart(2, '0')}:$minute $period"
    } catch (e: Exception) {
        time
    }

}

/**
 * Formats "YYYY-MM-DD" string to "MMM dd" (e.g. "Oct 12").
 */
fun formatDate(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString)
        val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        "$month ${date.dayOfMonth}"
    } catch (e: Exception) {
        dateString
    }
}
