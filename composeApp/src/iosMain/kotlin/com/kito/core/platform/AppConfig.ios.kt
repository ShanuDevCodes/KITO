package com.kito.core.platform

import platform.Foundation.NSBundle

actual object AppConfig {
    actual var portalBase: String
        get() = NSBundle.mainBundle.infoDictionary?.get("PORTAL_BASE") as? String ?: ""
        set(_) {} // iOS values come from Info.plist, setter is no-op

    actual var wdPath: String
        get() = NSBundle.mainBundle.infoDictionary?.get("WD_PATH") as? String ?: ""
        set(_) {} // iOS values come from Info.plist, setter is no-op

    actual var supabaseUrl: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_URL") as? String ?: ""
        set(_) {} // iOS values come from Info.plist, setter is no-op

    actual var supabaseAnonKey: String
        get() = NSBundle.mainBundle.infoDictionary?.get("SUPABASE_ANON_KEY") as? String ?: ""
        set(_) {} // iOS values come from Info.plist, setter is no-op
}

