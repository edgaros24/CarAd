package lt.viko.eif.ejurkoit.app.addcar

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import lt.viko.eif.ejurkoit.app.R

class AddCarActivity : AppCompatActivity() {

    private lateinit var editTextCarName: EditText
    private lateinit var editTextModel: EditText
    private lateinit var editTextEngine: EditText
    private lateinit var editTextWheels: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonCancel: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        editTextCarName = findViewById(R.id.editTextCarName)
        editTextModel = findViewById(R.id.editTextModel)
        editTextEngine = findViewById(R.id.editTextEngine)
        editTextWheels = findViewById(R.id.editTextWheels)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonCancel = findViewById(R.id.buttonCancel)
        firestore = FirebaseFirestore.getInstance()

        buttonAdd.setOnClickListener {
            addCarToFirestore()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun addCarToFirestore() {
        val carName = editTextCarName.text.toString()
        val model = editTextModel.text.toString()
        val engine = editTextEngine.text.toString()
        val wheels = editTextWheels.text.toString()

        val addSucc = getString(R.string.Add_Succ)
        val fillIn = getString(R.string.Fill_Fields)
        val errorAdd = getString(R.string.Error_Fill)

        if (carName.isEmpty() || model.isEmpty() || engine.isEmpty() || wheels.isEmpty()) {
            Toast.makeText(this, fillIn, Toast.LENGTH_SHORT).show()
            return
        }

        // Add the car to Firestore
        firestore.collection("Car")
            .add(mapOf("Name" to carName, "Model" to model, "Engine" to engine, "Wheels" to wheels))
            .addOnSuccessListener {
                Toast.makeText(this, addSucc, Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, errorAdd + exception.message, Toast.LENGTH_SHORT).show()
            }
    }
}
