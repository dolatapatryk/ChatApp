package pl.patrykdolata.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.login_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.services.SocketService

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        registerTextView.setOnClickListener {
            goToRegister()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketService.disconnect()
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        changeIntent(intent)
    }

    private fun changeIntent(intent: Intent) {
        Handler().postDelayed(
            {startActivity(intent)},
            10
        )
    }
}
