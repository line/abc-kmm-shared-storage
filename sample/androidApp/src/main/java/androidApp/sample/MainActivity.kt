package androidApp.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.linecorp.abc.sharedstorage.SharedStorage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Save */
        save()

        /* Load */
        updateView()

        /* Clear */
        findViewById<Button>(R.id.clear_button).run {
            setOnClickListener {
                SharedStorage.clearAllStorage()
                updateView()
            }
        }
    }

    private fun save() {
        SharedAppData.someInt = 501
        SharedAppData.someFloat = 501.5f
        SharedAppData.someLong = 500500L
        SharedAppData.someBoolean = true
        SharedAppData.someString = "I'm Some String"
        SharedAppData.someSecureString = "I'm Encrypted String"
    }

    private fun updateView() {
        findViewById<TextView>(R.id.text_view1).run { text = SharedAppData.someInt.toString() }
        findViewById<TextView>(R.id.text_view2).run { text = SharedAppData.someFloat.toString() }
        findViewById<TextView>(R.id.text_view3).run { text = SharedAppData.someLong.toString() }
        findViewById<TextView>(R.id.text_view4).run { text = SharedAppData.someBoolean.toString() }
        findViewById<TextView>(R.id.text_view5).run { text = SharedAppData.someString }
        findViewById<TextView>(R.id.text_view6).run { text = SharedAppData.someSecureString }
        findViewById<TextView>(R.id.all_data).run { text = "All Data: " + SharedStorage.getAll().toString() }
        findViewById<TextView>(R.id.all_secure_ata).run { text = "All Secure Data: " + SharedStorage.getAllSecure().toString() }
    }
}
