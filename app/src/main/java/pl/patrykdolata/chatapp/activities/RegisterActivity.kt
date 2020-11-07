package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.register_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.User
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.SocketUtils

class RegisterActivity : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

//        auth = FirebaseAuth.getInstance()

        textInputEditTextLogin.setText("patol")
        textInputEditTextPassword.setText("patryk")
        textInputEditTextPhone.setText("450006997")
        textInputEditTextEmail.setText("dolata.patryk1@gmail.com")

        registerButton.setOnClickListener {
            register()
        }

        loginTextView.setOnClickListener {
            goToLogin()
        }
    }

    private fun register() {
        val username = textInputEditTextLogin.text.toString().trim()
        val password = textInputEditTextPassword.text.toString().trim()
        val phone = textInputEditTextPhone.text.toString()
        val email = textInputEditTextEmail.text.toString().trim()
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val id = auth.currentUser?.uid
//                    val user = User(id.orEmpty(), username, email, phone)
//                    SocketService.emit(SocketUtils.registerEvent, user)
////                    Toast.makeText(this, "register success", Toast.LENGTH_LONG)
////                        .show()
//                } else {
//                    println(task.exception)
//                }
//            }
        Toast.makeText(this, "register success", Toast.LENGTH_LONG).show()
        goToLogin()
    }

    private fun goToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed(
            { startActivity(loginIntent) },
            10
        )
    }
}
