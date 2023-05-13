package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import com.example.myproject.databinding.FragmentFoodBinding
import com.example.myproject.model.Donation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentFoodBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_food, container, false)
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Realtime Database")
        val costBreakfast = arguments?.getDouble("costBreakfast", 0.0)
        val costLunch = arguments?.getDouble("costLunch")
        val costDinner = arguments?.getDouble("costDinner")
        val count = arguments?.getInt("count")
        val organization = arguments?.getString("organization")
        Log.d("costBreakfast", costBreakfast.toString())

        binding.typefoodSpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Log.d("Lunch", selectedItem)

                if (selectedItem == "Break fast") {
                    val costTotal = count?.let { costBreakfast?.times(it) }
                    binding.editfoodTextNumber.text = costTotal.toString()
                } else if (selectedItem == "Lunch") {
                    val costTotal = count?.let { costLunch?.times(it) }
                    binding.editfoodTextNumber.text = costTotal.toString()
                } else if(selectedItem == "Dinner") {
                    val costTotal = count?.let { costDinner?.times(it) }
                    binding.editfoodTextNumber.text = costTotal.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected if needed
            }
        }

        // Handle button click event
            binding.btnfood.setOnClickListener {
                // Get the selected date from the DatePicker
                val datePicker = binding.foodDateToDisplay
                val year = datePicker.year
                val month = datePicker.month
                val day = datePicker.dayOfMonth

                // Create a Calendar instance and set it to the selected date
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)

                // Convert the Calendar instance to a Date object
                //val date = calendar.time
                // Convert the Calendar instance to a Long value
                val dateStr = calendar.timeInMillis

                // Format the date string to the desired format
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = dateFormat.format(dateStr)

                val category="Food"
                val type = binding.typefoodSpinnerId.selectedItem.toString()
                val cost = binding.editfoodTextNumber.text.toString().toDouble()
                val note = binding.typefoodsTextMultiLine.text.toString()

                val donationId = database.child("donations").push().key

                // Create a new food object
                val donation = Donation(donationId,category,type, date, cost, note,items = null,imageUrl = null,organization)

                // Store the food object in the Firebase database with a unique ID
                if (donationId != null) {
                    database.child("donations").child(donationId).setValue(donation)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Food item added successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Clear input fields
                            // Get the current date
                            val cal = Calendar.getInstance()
                            val year = cal.get(Calendar.YEAR)
                            val month = cal.get(Calendar.MONTH)
                            val day = cal.get(Calendar.DAY_OF_MONTH)

                            // Set the date picker to the current date
                            binding.foodDateToDisplay.updateDate(year, month, day)
                            binding.typefoodSpinnerId.setSelection(0)
//                            binding.editfoodTextNumber.text.clear()
                            binding.typefoodsTextMultiLine.text.clear()
                        }.addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Error adding food item: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                val intent = Intent(context, DonationList::class.java)
                // Add any extra data to the intent if needed

                context?.startActivity(intent)
            }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoodFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}