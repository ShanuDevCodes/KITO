package com.kito

import android.app.Application
import com.kito.feature.schedule.notification.createClassNotificationChannel
import com.kito.core.di.initKoin
//import com.kito.feature.schedule.widget.scheduleDailyWidgetUpdate
import com.kito.core.platform.PlatformContext

class KitoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PlatformContext.init(this)
        initKoin(this)
//        scheduleDailyWidgetUpdate(this)
        createClassNotificationChannel(this)
    }
}
