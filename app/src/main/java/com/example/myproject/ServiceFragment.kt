package com.example.myproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myproject.databinding.FragmentServiceBinding
import com.example.myproject.model.Donation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentServiceBinding
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
        binding = FragmentServiceBinding.inflate(inflater, container, false)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Realtime Database")

        // Handle button click event
        binding.btnService.setOnClickListener {

            val category="Service"
            val type = binding.typeserviceSpinnerId.selectedItem.toString()
            val note = binding.typeServicesTextMultiLine.text.toString()

            val organization = arguments?.getString("organization")
            val donationId = database.child("donations").push().key

            // Create a new food object
            val donation = Donation(donationId,category,type, date=null, cost=null, note,items = null,imageUrl = null,organization)

            // Store the food object in the Firebase database with a unique ID
            if (donationId != null) {
                database.child("donations").child(donationId).setValue(donation)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Service item added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Clear input fields
                        binding.typeserviceSpinnerId.setSelection(0)
                        binding.typeServicesTextMultiLine.text.clear()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Error adding food item: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
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
         * @return A new instance of fragment ServiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServiceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}