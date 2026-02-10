package com.kito.core.common.util

import kotlin.time.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentLocalDateTime(): LocalDateTime {
    return Clock.System.now()
        .toEpochMilliseconds()
        .let { Instant.fromEpochMilliseconds(it) }
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
