package com.kito.feature.schedule.widget

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WidgetWorkScheduler {
    fun ensureWorkerScheduled(context: Context) {
        val appContext = context.applicationContext
        WorkManager.getInstance(appContext)
            .enqueueUniquePeriodicWork(
                workerName,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<WidgetTickWorker>(
                    15, TimeUnit.MINUTES
                ).build()
            )
    }
}

