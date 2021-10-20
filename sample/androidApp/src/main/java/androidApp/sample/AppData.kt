package androidApp.sample

import com.linecorp.abc.sharedstorage.annotations.Secure
import com.linecorp.abc.sharedstorage.annotations.SharedStorage

@SharedStorage
interface AppData {
    var someInt: Int
    var someFloat: Float
    val someLong: Long
    val someDouble: Double
    val someBoolean: Boolean
    val someString: String
    @Secure val someSecureString: String
}