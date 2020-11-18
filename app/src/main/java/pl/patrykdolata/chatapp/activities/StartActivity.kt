package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.start_activity.*
import pl.patrykdolata.chatapp.R

class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        val intent: Intent;
        val user = FirebaseAuth.getInstance().currentUser
        intent = if (user != null) {
            println(user.uid)
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startIntent(intent)
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
