package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.start_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.services.SocketService

class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        val mainIntent = Intent(this, LoginActivity::class.java)

        // todo - get logged user

        startIntent(mainIntent)
    }

    private fun startIntent(intent: Intent) {
        progressBar.visibility = View.VISIBLE
            Handler().postDelayed(
                {
                    startActivity(intent)
                    progressBar.visibility = View.GONE
                }, 2000
            )
    }
}
