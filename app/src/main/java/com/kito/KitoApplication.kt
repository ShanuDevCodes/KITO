package com.kito

import android.app.Application
import com.kito.feature.schedule.notification.createClassNotificationChannel
//import com.kito.feature.schedule.widget.scheduleDailyWidgetUpdate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KitoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
//        scheduleDailyWidgetUpdate(this)
        createClassNotificationChannel(this)
    }
}
