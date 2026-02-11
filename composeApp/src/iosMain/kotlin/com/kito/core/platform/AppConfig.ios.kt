package com.kito.core.platform

import platform.Foundation.NSBundle

actual object AppConfig {
    actual val portalBase: String
        get() = NSBundle.mainBundle.infoDictionary?.get("PORTAL_BASE") as? String ?: ""

    actual val wdPath: String
        get() = NSBundle.mainBundle.infoDictionary?.get("WD_PATH") as? String ?: ""

    actual val supabaseUrl: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_URL") as? String ?: ""

    actual val supabaseAnonKey: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_ANON_KEY") as? String ?: ""
}
