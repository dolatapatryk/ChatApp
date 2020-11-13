package pl.patrykdolata.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.services.SocketService

class LoginActivity : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

//        auth = FirebaseAuth.getInstance()

        textInputEditTextEmail.setText("dolata.patryk1@gmail.com")
        textInputEditTextPassword.setText("patryk")

        registerTextView.setOnClickListener {
            goToRegister()
        }

        loginButton.setOnClickListener {
            login()
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

    private fun login() {
        val email = textInputEditTextEmail.text.toString().trim()
        val password = textInputEditTextPassword.text.toString()

//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    Toast.makeText(this, "login success", Toast.LENGTH_LONG)
//                        .show()
//                    goToMainActivity()
//                } else {
//                    println(task.exception)
//                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        changeIntent(intent)
    }

    private fun changeIntent(intent: Intent) {
        Handler().postDelayed(
            {startActivity(intent)},
            10
        )
    }
}
