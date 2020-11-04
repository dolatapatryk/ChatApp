package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_activity.*
import pl.patrykdolata.chatapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        clickButton.setOnClickListener {
            textView.setText(auth.currentUser?.email)
            println(auth.currentUser)
            println(auth.currentUser?.email)
            println(auth.currentUser?.isEmailVerified)
            println(auth.currentUser?.uid)
        }

        logoutButton.setOnClickListener {
            logout()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        println("destroy main")
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed(
            {startActivity(intent)},
            10
        )
    }
}
