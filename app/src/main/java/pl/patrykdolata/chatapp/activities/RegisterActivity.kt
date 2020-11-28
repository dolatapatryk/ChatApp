package pl.patrykdolata.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.register_activity.*
import pl.patrykdolata.chatapp.R
import pl.patrykdolata.chatapp.models.User
import pl.patrykdolata.chatapp.services.SocketService
import pl.patrykdolata.chatapp.utils.Constants
import pl.patrykdolata.chatapp.utils.StringUtils

data class RegisterData(
    val username: String, val email: String, val password: String,
    val confirmPassword: String
)

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener { register() }
        loginTextView.setOnClickListener { goToLogin() }
    }

    private fun register() {
        progressBar.visibility = View.VISIBLE
        val username = textInputEditTextLogin.text.toString().trim()
        val email = textInputEditTextEmail.text.toString().trim()
        val password = textInputEditTextPassword.text.toString().trim()
        val confirmPassword = textInputEditTextConfirmPassword.text.toString().trim()

        val registerData = RegisterData(username, email, password, confirmPassword)
        if (isFormValid(registerData)) {
            SocketService.once(Constants.CHECK_USERNAME_RESPONSE_EVENT) { args ->
                onCheckUsernameResponse(registerData, args)
            }
            SocketService.emit(Constants.CHECK_USERNAME_EVENT, username)
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun onCheckUsernameResponse(registerData: RegisterData, responseArgs: Array<Any>) {
        runOnUiThread {
            val exists = responseArgs[0] as Boolean
            if (exists) {
                Toast.makeText(
                    this, "Podana nazwa użytkownika jest zajęta!",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            } else {
                createUser(registerData)
            }
        }
    }

    private fun createUser(registerData: RegisterData) {
        auth.createUserWithEmailAndPassword(registerData.email, registerData.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser?.uid
                    val user = User(id.orEmpty(), registerData.username, registerData.email)
                    SocketService.emit(Constants.REGISTER_EVENT, user)
                    Toast.makeText(
                        this, "Rejestracja przebiegła pomyślnie!",
                        Toast.LENGTH_LONG
                    ).show()
                    goToLogin()
                } else {
                    onFailedRegister(task.exception)
                }
                progressBar.visibility = View.GONE
            }
    }

    private fun goToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed(
            {
                startActivity(loginIntent)
                clearForm()
            },
            10
        )
    }

    private fun onFailedRegister(exception: Exception?) {
        val errorMessage = when (exception) {
            is FirebaseAuthUserCollisionException -> "Podany adres email jest zajęty!"
            is FirebaseAuthInvalidCredentialsException -> "Podany adres email jest niepoprawny!"
            is FirebaseAuthWeakPasswordException ->
                "Hasło za słabe! Hasło musi posiadać co najmniej 6 znaków."
            else -> "Rejestracja nie powiodła się"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun clearForm() {
        textInputEditTextLogin.setText("")
        textInputEditTextEmail.setText("")
        textInputEditTextPassword.setText("")
        textInputEditTextConfirmPassword.setText("")
    }

    private fun isFormValid(data: RegisterData): Boolean {
        return if (!checkField(data.username, "Wprowadź nazwę użytkownika!")) {
            false
        } else if (!checkField(data.email, "Wprowadź adres email!")) {
            false
        } else if (!checkField(data.password, "Wprowadź hasło!")) {
            false
        } else if (!checkField(data.confirmPassword, "Potwierdź hasło!")) {
            false
        } else if (data.password != data.confirmPassword) {
            Toast.makeText(this, "Hasła nie są zgodne!", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun checkField(field: String, errorMessage: String): Boolean {
        return if (StringUtils.isEmpty(field)) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}
