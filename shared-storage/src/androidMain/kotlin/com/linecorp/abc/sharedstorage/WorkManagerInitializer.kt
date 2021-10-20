package com.linecorp.abc.sharedstorage

import android.content.Context
import androidx.startup.Initializer

class WorkManagerInitializer : Initializer<Int> {

    override fun create(context: Context): Int {
        SharedStorage.configure(context, context.applicationContext.packageName)
        return 0
    }
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}