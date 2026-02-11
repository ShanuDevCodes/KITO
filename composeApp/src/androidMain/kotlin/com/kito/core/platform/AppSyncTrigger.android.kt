package com.kito.core.platform

import android.content.Context
import com.kito.core.database.entity.StudentSectionEntity
import com.kito.core.datastore.ProtoDataStoreProvider
import com.kito.core.datastore.StudentSectionDatastore
import com.kito.feature.schedule.notification.NotificationPipelineController
import com.kito.feature.schedule.widget.WidgetUpdater.nudgeRedraw
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class AppSyncTrigger(private val context: Context) {
    actual suspend fun onSyncComplete(rollNo: String, sections: List<StudentSectionEntity>) {
        // Write schedule data directly to ProtoDataStore (no DI)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Map common entities to Android DataStore DTOs
                val widgetData = sections.map { section ->
                    StudentSectionDatastore(
                        sectionId = section.sectionId,
                        rollNo = section.rollNo,
                        section = section.section,
                        batch = section.batch,
                        day = section.day,
                        startTime = section.startTime,
                        endTime = section.endTime,
                        subject = section.subject,
                        room = section.room
                    )
                }.toPersistentList()

                // Update DataStore directly via Provider (Singleton)
                ProtoDataStoreProvider.get(context).updateData {
                    it.copy(
                        list = widgetData,
                        rollNo = rollNo,
                        redrawToken = System.currentTimeMillis()
                    )
                }

                // Trigger Redraw
                nudgeRedraw(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        NotificationPipelineController.get(context).sync()
    }
}
