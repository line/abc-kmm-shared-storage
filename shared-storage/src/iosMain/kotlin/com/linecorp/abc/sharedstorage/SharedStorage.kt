package com.linecorp.abc.sharedstorage

import platform.Foundation.NSUserDefaults
import platform.Foundation.NSBundle
import keychain.Keychain

@Suppress("UNUSED")
actual class SharedStorage {

    actual companion object {

        // -------------------------------------------------------------------------------------------
        //  Constants
        // -------------------------------------------------------------------------------------------

        private const val serviceName: String = "kmm_shared_storage_prefs"

        // -------------------------------------------------------------------------------------------
        //  Actual
        // -------------------------------------------------------------------------------------------

        actual fun clearAllStorage() {
            clearSharedStorage()
            clearSecureStorage()
        }

        actual fun clearSharedStorage() {
            val bundleId = getBundleIdentifier()
            NSUserDefaults.standardUserDefaults.removePersistentDomainForName(bundleId)
        }

        actual fun clearSecureStorage() {
            Keychain.clear()
        }

        actual fun getAll(): Map<String, *> {
            val bundleId = getBundleIdentifier()
            val map = mutableMapOf<String, Any>()
            val domain = NSUserDefaults.standardUserDefaults.persistentDomainForName(bundleId)?.let { it } ?: return map
            for ((key, value) in domain.asIterable()) {
                if (key == null || value == null) { continue }
                map[key as String] = value
            }
            return map
        }

        @Suppress("UNCHECKED_CAST")
        actual fun getAllSecure(): Map<String, *> {
            val keychainMap = Keychain.getAllPasswordForService(serviceName)?.toMap()
            return keychainMap as? Map<String, *> ?: emptyMap<String, Any>()
        }

        @Suppress("UNCHECKED_CAST")
        actual fun <T> load(key: String, default: T) =
            NSUserDefaults.standardUserDefaults.objectForKey(key) as? T ?: default

        actual fun <T> save(value: T, forKey: String) {
            NSUserDefaults.standardUserDefaults.setObject(value, forKey)
            NSUserDefaults.standardUserDefaults.synchronize()
        }

        actual fun secureLoad(key: String, default: String) =
            Keychain.passwordForService(serviceName, key) ?: default

        actual fun secureSave(value: String, forKey: String) {
            Keychain.setPassword(value, serviceName, forKey)
        }

        // -------------------------------------------------------------------------------------------
        //  Private
        // -------------------------------------------------------------------------------------------

        private fun getBundleIdentifier() =
            NSBundle.mainBundle.bundleIdentifier?.let { it } ?: ""
    }
}