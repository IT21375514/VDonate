package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.model.Donation
import com.example.myproject.model.Organization
import com.example.myproject.view.DonationAdapter
import com.example.myproject.view.OrganizationAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var recv: RecyclerView
    private lateinit var organizationList: ArrayList<Organization>
    private lateinit var organizationAdapter: OrganizationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_list)

        FirebaseApp.initializeApp(this) // Initialize Firebase

        recv = findViewById(R.id.orgRecycler)
        recv.layoutManager = LinearLayoutManager(this)
        recv.setHasFixedSize(true)

        organizationList = arrayListOf()
        organizationAdapter = OrganizationAdapter(applicationContext, organizationList)
        recv.adapter = organizationAdapter


        getUserData()
    }
    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("/Realtime Database/organizations")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    organizationList.clear() // Clear the existing list before populating it with new data
                    for (organizationSnapshot in snapshot.children) {
                        Log.d("OrganizationList", "OrganizationSnapshot: $organizationSnapshot")
                        val organization = organizationSnapshot.getValue(Organization::class.java)
                        organization?.let {
                            organizationList.add(it)
                        }
                    }
                    organizationAdapter.notifyDataSetChanged() // Notify the adapter of the data changes
                } else {
                    Log.d("OrganizationList", "No data available")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OrganizationList", "Firebase Database Error: ${error.message}")
            }
        })

    }
}