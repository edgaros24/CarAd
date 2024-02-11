package lt.viko.eif.ejurkoit.app.cardetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.FirebaseFirestore
import lt.viko.eif.ejurkoit.app.R
import lt.viko.eif.ejurkoit.app.update.UpdateCarActivity

class CarModelsAdapter(context: Context, private val resource: Int, private val cars: List<Car>) :
    ArrayAdapter<Car>(context, resource, cars) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(resource, parent, false)

        val car = getItem(position)
        val textViewModel: TextView = view.findViewById(R.id.textViewModel)
        val buttonUpdateModel: Button = view.findViewById(R.id.buttonUpdateModel)
        val buttonDeleteModel: Button = view.findViewById(R.id.buttonDeleteModel)

        textViewModel.text = "Model: ${car?.model}\nEngine: ${car?.engine}\nWheels: ${car?.wheels}"

        buttonUpdateModel.setOnClickListener {
            val intent = Intent(context, UpdateCarActivity::class.java)

            // Pass the necessary data to the UpdateCarActivity
            intent.putExtra("DocumentId", car?.documentId)
            intent.putExtra("originalModel", car?.model)
            intent.putExtra("originalEngine", car?.engine)
            intent.putExtra("originalWheels", car?.wheels)

            context.startActivity(intent)
        }

        buttonDeleteModel.setOnClickListener {
            deleteCar(position)
        }

        return view
    }

    private fun deleteCar(position: Int) {
        val deletedCar = getItem(position)

        // Update Firestore to delete the corresponding document
        val firestore = FirebaseFirestore.getInstance()
        val carRef = firestore.collection("Car").document(deletedCar?.documentId ?: "")

        carRef.delete()
            .addOnSuccessListener {
                remove(deletedCar)
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}
