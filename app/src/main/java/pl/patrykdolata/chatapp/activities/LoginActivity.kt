package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.login_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.SocketConstants
import pl.patrykdolata.chatapp.utils.StringUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        auth = FirebaseAuth.getInstance()

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

    private fun login() {
        progressBar.visibility = View.VISIBLE
        val email = textInputEditTextEmail.text.toString().trim()
        val password = textInputEditTextPassword.text.toString()

        if (isFormValid(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Pomyślne logowanie!", Toast.LENGTH_LONG)
                            .show()
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { token ->
                            if (token.isSuccessful) {
                                val tokenString = token.result ?: ""
//                                println("token - $tokenString")
                                SocketService.emit(SocketConstants.LOGIN_EVENT, userId, tokenString)
                            }
                        }
                        goToMainActivity()
                    } else {
                        onFailedLogin(task.exception)
                    }
                    progressBar.visibility = View.GONE
                }
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun onFailedLogin(exception: Exception?) {
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException ->
                "Wprowadzono nieprawidłowy adres email lub hasło!"
            else -> "Logowanie nie powiodło się!"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        changeIntent(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        changeIntent(intent)
    }

    private fun changeIntent(intent: Intent) {
        Handler().postDelayed(
            {
                startActivity(intent)
                clearForm()
            },
            10
        )
    }

    private fun isFormValid(email: String, password: String): Boolean {
        return if (!checkField(email, "Wprowadź adres email!")) {
            false
        } else checkField(password, "Wprowadź hasło!")
    }

    private fun checkField(field: String, errorMessage: String): Boolean {
        return if (StringUtils.isEmpty(field)) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun clearForm() {
        textInputEditTextEmail.setText("")
        textInputEditTextPassword.setText("")
    }
}
