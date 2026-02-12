package com.kito.core.platform

import com.kito.core.database.entity.StudentSectionEntity

actual class AppSyncTrigger {
    actual suspend fun onSyncComplete(rollNo: String, sections: List<StudentSectionEntity>) {
        // No-op on iOS — no widgets or Android notifications to update
    }
}
