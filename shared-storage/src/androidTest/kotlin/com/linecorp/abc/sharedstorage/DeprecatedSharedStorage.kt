package com.linecorp.abc.sharedstorage

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("UNCHECKED_CAST")
class DeprecatedSharedStorage {

    companion object {

        private const val password = "com.linecorp.abc.sharedstorage.hanpro"
        private val commitEditorSynchronously = AtomicBoolean(false)

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var encryptedPreferences: SharedPreferences

        fun <T: Any>save(value: T, forKey: String) {
            saveData(value, forKey, false)
        }

        fun <T: Any>load(key: String, default: T): T {
            return loadData(key, default, false)
        }

        fun secureSave(value: String, forKey: String) {
            saveData(value, forKey, true)
        }

        fun secureLoad(key: String, default: String): String {
            return loadData(key, default, true)
        }

        fun getAll(): Map<String, *> {
            return sharedPreferences.all
        }

        fun getAllSecure(): Map<String, *> {
            return encryptedPreferences.all
        }

        fun clearAllStorage() {
            clearSharedStorage()
            clearSecureStorage()
        }

        fun clearSharedStorage() {
            val editor = sharedPreferences.edit()
            editor.clear()
            commit(editor)
        }

        fun clearSecureStorage() {
            val editor = encryptedPreferences.edit()
            editor.clear()
            commit(editor)
        }

        fun configure(context: Context, preferenceName: String = "com.linecorp.SharedStore", preferenceMode: Int = Context.MODE_PRIVATE) {
            /* Shared Preference */
            sharedPreferences = context.getSharedPreferences(
                preferenceName,
                preferenceMode
            )

            /* Secure Preference */
            encryptedPreferences = SecurePreferences(
                context,
                password,
                preferenceName + "_encrypted"
            )
        }

        private fun <T: Any>saveData(value: T, forKey: String, secure: Boolean) {
            val editor: SharedPreferences.Editor = if (secure) {
                encryptedPreferences.edit()
            } else {
                sharedPreferences.edit()
            }
            when (value) {
                is Int -> {
                    editor.putInt(forKey, (value as Int?)!!)
                }
                is Boolean -> {
                    editor.putBoolean(forKey, (value as Boolean?)!!)
                }
                is Long -> {
                    editor.putLong(forKey, (value as Long?)!!)
                }
                is Float -> {
                    editor.putFloat(forKey, (value as Float?)!!)
                }
                else -> {
                    editor.putString(forKey, value.toString())
                }
            }
            commit(editor)
        }

        private fun <T: Any>loadData(key: String, default: T, secure: Boolean): T {
            val preference: SharedPreferences = if (secure) {
                encryptedPreferences
            } else {
                sharedPreferences
            }
            if (preference.contains(key)) {
                return when (default) {
                    is Int -> {
                        preference.getInt(key, default) as T
                    }
                    is Boolean -> {
                        preference.getBoolean(key, default) as T
                    }
                    is Long -> {
                        preference.getLong(key, default) as T
                    }
                    is Float -> {
                        preference.getFloat(key, default) as T
                    }
                    else -> {
                        preference.getString(key, default.toString()) as T
                    }
                }
            }
            return default
        }

        private fun commit(editor: SharedPreferences.Editor) {
            if (!commitEditorSynchronously.get()) {
                editor.apply()
                return
            }
            editor.commit()
        }
    }
}