package com.kito.core.platform

expect object AppConfig {
    val portalBase: String
    val wdPath: String
    val supabaseUrl: String
    val supabaseAnonKey: String
}
