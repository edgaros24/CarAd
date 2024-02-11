package lt.viko.eif.ejurkoit.app.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import lt.viko.eif.ejurkoit.app.MainMenuActivity
import lt.viko.eif.ejurkoit.app.R

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val signInButton: Button = findViewById(R.id.buttonSignIn)
        val signUpButton: Button = findViewById(R.id.buttonSignUp)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call Firebase Authentication to sign in
            signInWithEmailPassword(email, password)
        }
        signUpButton.setOnClickListener {
            // Navigate to the SignUpActivity when the "Sign Up" button is clicked
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInWithEmailPassword(email: String, password: String) {
        val errorTextView: TextView = findViewById(R.id.textViewError)
        val signInSucc = getString(R.string.toast_sign_In_succ)
        val fillIn = getString(R.string.Fill_In)
        if(email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Toast.makeText(this, signInSucc, Toast.LENGTH_SHORT).show()
                        errorTextView.text = ""
                        val intent = Intent(this, MainMenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            errorTextView.text = fillIn
        }
    }
}