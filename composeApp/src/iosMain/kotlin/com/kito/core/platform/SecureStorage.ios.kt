package com.kito.core.platform

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.CoreFoundation.CFAutorelease
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.errSecItemNotFound
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleWhenUnlockedThisDeviceOnly
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

actual class SecureStorage {
    private companion object {
        const val SERVICE_NAME = "com.kito.securestorage"
        const val KEY_SAP_PASSWORD = "sap_password"
    }

    private val _isLoggedIn = MutableStateFlow(hasPassword())
    actual val isLoggedInFlow: Flow<Boolean> = _isLoggedIn

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveSapPassword(password: String): Boolean {
        val data = (password as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return false

        // Delete existing item first (if any)
        deleteKeychainItem(KEY_SAP_PASSWORD)

        val status = addKeychainItem(KEY_SAP_PASSWORD, data)
        val success = status == errSecSuccess
        if (success) {
            _isLoggedIn.value = true
        }
        return success
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun getSapPassword(): String {
        val data = queryKeychainItem(KEY_SAP_PASSWORD) ?: return ""
        return NSString.create(data = data, encoding = NSUTF8StringEncoding)?.toString() ?: ""
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun clearSapPassword(): Boolean {
        val status = deleteKeychainItem(KEY_SAP_PASSWORD)
        val success = status == errSecSuccess || status == errSecItemNotFound
        if (success) {
            _isLoggedIn.value = false
        }
        return success
    }

    // ── Keychain Helpers ──────────────────────────────────────────────

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun addKeychainItem(account: String, data: NSData): Int {
        val query = keychainQuery(account)
        CFDictionaryAddValue(query, kSecValueData, CFBridgingRetain(data))
        CFDictionaryAddValue(
            query,
            kSecAttrAccessible,
            kSecAttrAccessibleWhenUnlockedThisDeviceOnly
        )
        val status = SecItemAdd(query, null)
        CFAutorelease(query)
        return status
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun queryKeychainItem(account: String): NSData? = memScoped {
        val query = keychainQuery(account)
        CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

        val result = alloc<CFTypeRefVar>()
        val status = SecItemCopyMatching(query, result.ptr)
        CFAutorelease(query)

        if (status == errSecSuccess) {
            CFBridgingRelease(result.value) as? NSData
        } else {
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun deleteKeychainItem(account: String): Int {
        val query = keychainQuery(account)
        val status = SecItemDelete(query)
        CFAutorelease(query)
        return status
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun hasPassword(): Boolean {
        val data = queryKeychainItem(KEY_SAP_PASSWORD)
        return data != null
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun keychainQuery(account: String): kotlinx.cinterop.CPointer<platform.CoreFoundation.CFMutableDictionaryRef?> {
        val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 4, null, null)!!
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(SERVICE_NAME as NSString))
        CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(account as NSString))
        return query
    }
}
