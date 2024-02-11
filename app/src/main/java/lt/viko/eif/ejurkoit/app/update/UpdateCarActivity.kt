package lt.viko.eif.ejurkoit.app.update

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import lt.viko.eif.ejurkoit.app.R

class UpdateCarActivity : AppCompatActivity() {

    private lateinit var editTextModel: EditText
    private lateinit var editTextEngine: EditText
    private lateinit var editTextWheels: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonCancel: Button

    private lateinit var firestore: FirebaseFirestore

    private lateinit var carDocumentId: String
    private lateinit var originalModel: String
    private lateinit var originalEngine: String
    private lateinit var originalWheels: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_car)

        editTextModel = findViewById(R.id.editTextModel)
        editTextEngine = findViewById(R.id.editTextEngine)
        editTextWheels = findViewById(R.id.editTextWheels)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonCancel = findViewById(R.id.buttonCancel)

        firestore = FirebaseFirestore.getInstance()

        carDocumentId = intent.getStringExtra("DocumentId") ?: ""
        originalModel = intent.getStringExtra("originalModel") ?: ""
        originalEngine = intent.getStringExtra("originalEngine") ?: ""
        originalWheels = intent.getStringExtra("originalWheels") ?: ""

        // Pre-fill the EditText fields with original values
        editTextModel.setText(originalModel)
        editTextEngine.setText(originalEngine)
        editTextWheels.setText(originalWheels)

        buttonUpdate.setOnClickListener {
            updateCarDetails()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateCarDetails() {
        val updatedModel = editTextModel.text.toString()
        val updatedEngine = editTextEngine.text.toString()
        val updatedWheels = editTextWheels.text.toString()
        val updSucc = getString(R.string.Update_Succ)

        if (updatedModel == originalModel && updatedEngine == originalEngine && updatedWheels == originalWheels) {
            finish()
        } else {
            // Update the document in Firestore with the new values
            val carRef = firestore.collection("Car").document(carDocumentId)
            carRef.update(
                "Model", updatedModel,
                "Engine", updatedEngine,
                "Wheels", updatedWheels
            )
                .addOnSuccessListener {
                    // Update successful
                    Toast.makeText(this, updSucc, Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Update failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
