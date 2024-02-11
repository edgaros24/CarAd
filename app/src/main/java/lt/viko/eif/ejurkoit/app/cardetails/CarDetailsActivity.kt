package lt.viko.eif.ejurkoit.app.cardetails

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import lt.viko.eif.ejurkoit.app.R
import lt.viko.eif.ejurkoit.app.update.UpdateCarActivity

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private val carCollection = FirebaseFirestore.getInstance().collection("Car")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        val listViewCarModels: ListView = findViewById(R.id.listViewCarModels)
        val buttonGoBack: Button = findViewById(R.id.buttonGoBack)

        buttonGoBack.setOnClickListener {
            finish()
        }

        // Retrieve car details from intent
        val carName = intent.getStringExtra("Name") ?: ""

        // Retrieve car details from Firestore
        carCollection.whereEqualTo("Name", carName)
            .get()
            .addOnSuccessListener { result ->
                val cars = result.documents.mapNotNull { document ->
                    val documentId = document.id
                    val model = document.getString("Model") ?: ""
                    val engine = document.getString("Engine") ?: ""
                    val wheels = document.getString("Wheels") ?: ""
                    Car(documentId, model, engine, wheels)
                }

                // Set up ListView with a custom adapter
                val adapter = CarModelsAdapter(this, R.layout.item_car_model, cars)
                listViewCarModels.adapter = adapter

                // Handle item click events if needed
                listViewCarModels.setOnItemClickListener { _, _, position, _ ->
                    val selectedCar = cars[position]
                    // Handle item click as needed
                }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error retrieving car models: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
