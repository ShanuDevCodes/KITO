package com.kito.core.platform

import android.content.Context
import com.kito.feature.schedule.notification.NotificationPipelineController
import com.kito.feature.schedule.widget.WidgetUpdater.nudgeRedraw

actual class AppSyncTrigger(private val context: Context) {
    actual suspend fun onSyncComplete() {
        nudgeRedraw(context)
        NotificationPipelineController.get(context).sync()
    }
}
