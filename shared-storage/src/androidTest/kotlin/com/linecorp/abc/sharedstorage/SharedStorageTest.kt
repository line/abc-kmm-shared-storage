package com.linecorp.abc.sharedstorage

import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [android.os.Build.VERSION_CODES.LOLLIPOP]
)
class SharedStorageTest {

    fun configureSharedStorage() {
        SharedStorage.configure(ApplicationProvider.getApplicationContext(), this::class.simpleName.toString())
    }

    @Test
    fun testStoreInt() {
        configureSharedStorage()
        val value = 100
        SharedStorage.save(value, "key_int")
        assertEquals(value, SharedStorage.load("key_int", 0))
    }

    @Test
    fun testStoreFloat() {
        configureSharedStorage()
        val value = 101.0f
        SharedStorage.save(value, "key_float")
        assertEquals(value, SharedStorage.load("key_float", 0.0f))
    }

    @Test
    fun testStoreLong() {
        configureSharedStorage()
        val value = 102L
        SharedStorage.save(value, "key_long")
        assertEquals(value, SharedStorage.load("key_long", 0L))
    }

    @Test
    fun testStoreBoolean() {
        configureSharedStorage()
        val value = true
        SharedStorage.save(value, "key_boolean")
        assertEquals(value, SharedStorage.load("key_boolean", false))
    }

    @Test
    fun testStoreString() {
        configureSharedStorage()
        val value = "Im String"
        SharedStorage.save(value, "key_string")
        assertEquals(value, SharedStorage.load("key_string", ""))
    }

    @Test
    fun testStoreSecureString() {
        configureSharedStorage()
        val value = "Im Secure String"
        SharedStorage.secureSave(value, "key_secure")
        assertEquals(value, SharedStorage.secureLoad("key_secure", ""))
    }

    @Test
    fun testCountStorage() {
        configureSharedStorage()
        SharedStorage.save(1, "key_int")
        SharedStorage.save(2.0f, "key_float")
        SharedStorage.save(3L, "key_long")
        SharedStorage.save(true, "key_boolean")
        SharedStorage.save("4", "key_string")
        SharedStorage.secureSave("secure", "key_string")
        assertEquals(5, SharedStorage.getAll().size)
        assertEquals(1, SharedStorage.getAllSecure().size)
    }

    @Test
    fun testClearStorage() {
        configureSharedStorage()
        SharedStorage.save(1, "key_int")
        SharedStorage.save(2.0f, "key_float")
        assertEquals(2, SharedStorage.getAll().size)

        SharedStorage.clearSharedStorage()
        assertEquals(0, SharedStorage.getAll().size)
    }

    @Test
    fun testClearSecureStorage() {
        configureSharedStorage()
        SharedStorage.secureSave("secure", "key_string")
        assertEquals(1, SharedStorage.getAllSecure().size)

        SharedStorage.clearSecureStorage()
        assertEquals(0, SharedStorage.getAllSecure().size)
    }

    @Test
    fun testClearAllStorage() {
        configureSharedStorage()
        SharedStorage.save(1, "key_int")
        SharedStorage.save(2.0f, "key_float")
        assertEquals(2, SharedStorage.getAll().size)

        SharedStorage.secureSave("secure", "key_string")
        assertEquals(1, SharedStorage.getAllSecure().size)

        SharedStorage.clearAllStorage()
        assertEquals(0, SharedStorage.getAll().size)
        assertEquals(0, SharedStorage.getAllSecure().size)
    }

    @Test
    fun testMigration() {
        DeprecatedSharedStorage.configure(ApplicationProvider.getApplicationContext())
        DeprecatedSharedStorage.clearAllStorage()
        DeprecatedSharedStorage.save(1, "key_int")
        DeprecatedSharedStorage.save(2.0f, "key_float")
        DeprecatedSharedStorage.save(3L, "key_long")
        DeprecatedSharedStorage.save(true, "key_boolean")
        DeprecatedSharedStorage.save("4", "key_string")
        DeprecatedSharedStorage.secureSave("secure", "key_string")

        configureSharedStorage()

        assertEquals(0, DeprecatedSharedStorage.getAll().size)
        assertEquals(0, DeprecatedSharedStorage.getAllSecure().size)

        assertEquals(1, SharedStorage.load("key_int", 0))
        assertEquals(2.0f, SharedStorage.load("key_float", 0f))
        assertEquals(3L, SharedStorage.load("key_long", 0))
        assertEquals(true, SharedStorage.load("key_boolean", false))
        assertEquals("4", SharedStorage.load("key_string", ""))
        assertEquals("secure", SharedStorage.secureLoad("key_string", ""))
    }
}