package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.model.Donation
import com.example.myproject.view.DonationAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.*

class DonationList : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var recv: RecyclerView
    private lateinit var donationList:ArrayList<Donation>
    private lateinit var donationAdapter: DonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        FirebaseApp.initializeApp(this) // Initialize Firebase

        recv = findViewById(R.id.mRecycler)
        recv.layoutManager = LinearLayoutManager(this)
        recv.setHasFixedSize(true)

        donationList = arrayListOf()
        donationAdapter = DonationAdapter(applicationContext, donationList)
        recv.adapter = donationAdapter

        getUserData()
    }


    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("/Realtime Database/donations")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    donationList.clear() // Clear the existing list before populating it with new data
                    for (donationSnapshot in snapshot.children) {
                        Log.d("DonationList", "DonationSnapshot: $donationSnapshot")
                        val donation = donationSnapshot.getValue(Donation::class.java)
                        donation?.let {
                            donationList.add(it)
                        }
                    }
                    donationAdapter.notifyDataSetChanged() // Notify the adapter of the data changes
                } else {
                    Log.d("DonationList", "No data available")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DonationList", "Firebase Database Error: ${error.message}")
            }
        })

    }
}