package com.kito.core.platform

import com.kito.core.database.entity.StudentSectionEntity

/**
 * Platform-specific trigger for post-sync actions (widget refresh, notification reschedule).
 * Android: calls WidgetUpdater.nudgeRedraw() + NotificationPipelineController.sync()
 * iOS: no-op for now
 */
expect class AppSyncTrigger {
    suspend fun onSyncComplete(rollNo: String, sections: List<StudentSectionEntity>)
}
