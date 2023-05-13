package com.example.myproject

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myproject.databinding.FragmentGoodsBinding
import com.example.myproject.model.Donation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoodsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoodsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentGoodsBinding

    // Initialize Firebase database reference
    private lateinit var database: DatabaseReference

    // Initialize Firebase storage reference
    private var storageReference: StorageReference? = null

    // Initialize the image URL variable
    private var imageUrl: String? = null

    // Initialize the image name variable
    private var imageName: String? = null

    // Initialize the pick image request code constant
    private val PICK_IMAGE_REQUEST_CODE = 1

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
        binding = FragmentGoodsBinding.inflate(inflater, container, false)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Realtime Database")

        // Initialize Firebase storage reference
        storageReference = FirebaseStorage.getInstance().reference

        // Set an onClickListener for the image view to select an image
        binding.uploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        // Set an onClickListener for the submit button
        binding.btnGoods.setOnClickListener {
            val category="Goods"
            val type = binding.typegoodsSpinnerId.selectedItem.toString()
            val note = binding.typegoodsTextMultiLine.text.toString()
            val items = binding.editTextTextMultiLine2.text.toString()

            val organization = arguments?.getString("organization")
            val donationId = database.child("donations").push().key

            // Create a new food object with the image URL if available
            val donation = if (imageUrl != null) {
                Donation(donationId,category,type, date=null, cost=null, note,items,imageUrl,organization)
            } else {
                Donation(donationId,category,type, date=null, cost=null, note,items)
            }

            // Store the food object in the Firebase database with a unique ID
            if (donationId != null) {
                database.child("donations").child(donationId).setValue(donation)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Good item added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.typegoodsSpinnerId.setSelection(0)
                        binding.typegoodsTextMultiLine.text.clear()
                        binding.editTextTextMultiLine2.text.clear()
                        binding.uploadImage.setImageResource(android.R.color.transparent) // Clear the selected image
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            val filePath = storageReference!!.child("images").child("$imageName.jpg")
            if (imageUri != null) {
                filePath.putFile(imageUri).addOnSuccessListener {
                    filePath.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                        // The image has been uploaded to Firebase Storage, you can now save the donation object to the database with the image URL
                        // Set the selected image to the ImageView
                        binding.uploadImage.setImageURI(imageUri)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GoodsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GoodsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}