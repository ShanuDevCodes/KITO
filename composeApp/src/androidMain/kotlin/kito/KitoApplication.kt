package com.kito

import android.app.Application
import com.kito.feature.schedule.notification.createClassNotificationChannel
import com.kito.core.di.initKoin
//import com.kito.feature.schedule.widget.scheduleDailyWidgetUpdate
class KitoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
//        scheduleDailyWidgetUpdate(this)
        createClassNotificationChannel(this)
    }
}
