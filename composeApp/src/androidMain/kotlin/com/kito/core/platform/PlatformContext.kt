package com.kito.core.platform

import android.annotation.SuppressLint
import android.content.Context

/**
 * Singleton holder for the Android application context.
 * Initialized in KitoApplication.onCreate() — available app-wide.
 */
@SuppressLint("StaticFieldLeak")
object PlatformContext {
    var applicationContext: Context? = null
        private set

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}
