package com.kito.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TimeTableAppWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = TimetableWidget()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // First widget added
        scheduleDailyWidgetUpdate(context)
    }
    override fun onDeleted(context: Context, widgetIds: IntArray) {
        super.onDeleted(context, widgetIds)
        // Optional: cancel work when last widget removed
        cancelDailyWidgetUpdate(context)
    }
}
