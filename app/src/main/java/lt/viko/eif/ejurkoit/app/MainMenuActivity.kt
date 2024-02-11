package lt.viko.eif.ejurkoit.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import lt.viko.eif.ejurkoit.app.addcar.AddCarActivity
import lt.viko.eif.ejurkoit.app.cardetails.CarDetailsActivity
import lt.viko.eif.ejurkoit.app.signin.SignInActivity

class MainMenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val signOutButton: Button = findViewById(R.id.buttonSignOut)
        val db = FirebaseFirestore.getInstance()
        val carCollection = db.collection("Car")
        val linearContainer: LinearLayout = findViewById(R.id.linearContainer)
        val addButton: Button = findViewById(R.id.buttonAddCar)
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
            swipeRefreshLayout.isRefreshing = false
        }
        addButton.setOnClickListener {
            navigateToAddCar()
        }
        auth = FirebaseAuth.getInstance()

        signOutButton.setOnClickListener {
            signOutUser()
        }

        val uniqueCarNames = HashSet<String>()
        // Retrieve data from Firestore
        carCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Access the data from the document
                    val carName = document.getString("Name")

                    // Check if the car name is not already added
                    if (carName != null && !uniqueCarNames.contains(carName)) {
                        val button = Button(this)
                        button.text = carName
                        button.setOnClickListener {
                            navigateToCarDetails(carName)
                        }
                        linearContainer.addView(button)

                        uniqueCarNames.add(carName)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error retrieving data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signOutUser() {
        auth.signOut()

        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToCarDetails(carName: String?) {
        val intent = Intent(this, CarDetailsActivity::class.java).apply {
            putExtra("Name", carName)
        }
        startActivity(intent)
    }

    private fun navigateToAddCar() {
        val intent = Intent(this, AddCarActivity::class.java)
        startActivity(intent)
    }
    private fun fetchData(){
        val linearContainer: LinearLayout = findViewById(R.id.linearContainer)
        // Use HashSet to store unique car names
        val uniqueCarNames = HashSet<String>()

        // Retrieve data from Firestore
        val db = FirebaseFirestore.getInstance()
        val carCollection = db.collection("Car")

        carCollection.get()
            .addOnSuccessListener { result ->
                // Clear existing views in the linear container
                linearContainer.removeAllViews()

                for (document in result) {
                    val carName = document.getString("Name")

                    if (carName != null && !uniqueCarNames.contains(carName)) {
                        val button = Button(this)
                        button.text = carName
                        button.setOnClickListener {
                            navigateToCarDetails(carName)
                        }

                        // Add the Button to the linear container
                        linearContainer.addView(button)

                        // Add the car name to the HashSet
                        uniqueCarNames.add(carName)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error retrieving data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
