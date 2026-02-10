package com.kito.core.platform

actual object AppConfig {
    actual val portalBase: String = "" // TODO: Read from iOS plist or config
    actual val wdPath: String = ""
    actual val supabaseUrl: String = ""
    actual val supabaseAnonKey: String = ""
}
