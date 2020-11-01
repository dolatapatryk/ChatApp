package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import pl.patrykdolata.chatapp.R

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        val mainIntent = Intent(this, LoginActivity::class.java)
        supportActionBar?.hide()

        // todo - get logged user

        startIntent(mainIntent)
    }

    private fun startIntent(intent: Intent) {
        Handler().postDelayed(
            { startActivity(intent) }, 3000
        )
    }
}
