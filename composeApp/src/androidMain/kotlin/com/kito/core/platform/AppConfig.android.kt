package com.kito.core.platform

import com.kito.BuildConfig

actual object AppConfig {
    actual val portalBase: String = BuildConfig.PORTAL_BASE
    actual val wdPath: String = BuildConfig.WD_PATH
    actual val supabaseUrl: String = BuildConfig.SUPABASE_URL
    actual val supabaseAnonKey: String = BuildConfig.SUPABASE_ANON_KEY
}
