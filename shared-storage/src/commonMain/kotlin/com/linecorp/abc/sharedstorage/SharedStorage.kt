package com.linecorp.abc.sharedstorage

expect class SharedStorage {

    companion object {
        fun clearAllStorage()
        fun clearSharedStorage()
        fun clearSecureStorage()
        fun getAll(): Map<String, *>
        fun getAllSecure(): Map<String, *>
        fun <T>load(key: String, default: T): T
        fun <T>save(value: T, forKey: String)
        fun secureLoad(key: String, default: String): String
        fun secureSave(value: String, forKey: String)
    }
}