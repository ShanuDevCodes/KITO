package com.kito.core.platform

actual class AppSyncTrigger {
    actual suspend fun onSyncComplete() {
        // No-op on iOS — no widgets or Android notifications to update
    }
}
