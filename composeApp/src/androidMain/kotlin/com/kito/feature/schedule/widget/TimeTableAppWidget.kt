package com.kito.feature.schedule.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.WorkManager

class TimeTableAppWidget : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = TimetableWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        WidgetWorkScheduler.ensureWorkerScheduled(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("Widget", "onEnabled Called")
        WidgetWorkScheduler.ensureWorkerScheduled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("Widget", "onDisabled Called")

        val widgetIds = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(
                android.content.ComponentName(
                    context,
                    TimeTableAppWidget::class.java
                )
            )

        if (widgetIds.isEmpty()) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(workerName)
        }
    }
}

