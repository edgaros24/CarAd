package lt.viko.eif.ejurkoit.app.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import lt.viko.eif.ejurkoit.app.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        auth = FirebaseAuth.getInstance()

        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPasswordSignUp: EditText = findViewById(R.id.editTextPasswordSignUp)
        val buttonSignUpSubmit: Button = findViewById(R.id.buttonSignUpSubmit)
        val buttonBackToSignIn: Button = findViewById(R.id.buttonBackToSignIn)

        buttonSignUpSubmit.setOnClickListener {
            signUpUser(editTextEmail.text.toString(), editTextPasswordSignUp.text.toString())
        }
        buttonBackToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpUser(email: String, password: String) {
        val errorTextView: TextView = findViewById(R.id.textViewError)
        val signUpSuccess = getString(R.string.message_sign_up_success)
        val textEmpty = getString(R.string.message_sign_up_empty)
        val signUpFailed = getString(R.string.sign_up_failed)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, signUpSuccess, Toast.LENGTH_SHORT).show()
                        errorTextView.text = ""
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        errorTextView.text = signUpFailed + task.exception?.message
                    }
                }
        }
        else {
            errorTextView.text = textEmpty
        }
        }
    }

