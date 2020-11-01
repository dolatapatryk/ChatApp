package pl.patrykdolata.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.register_activity.*
import pl.patrykdolata.chatapp.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        loginTextView.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed(
            {startActivity(loginIntent)},
            10
        )
    }
}
