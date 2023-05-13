package com.example.myproject.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.ActivityDonate
import com.example.myproject.R
import com.example.myproject.model.Organization
import com.squareup.picasso.Picasso

class OrganizationAdapter(private val c: Context, private val organizationList:ArrayList<Organization>):
    RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {


    inner class OrganizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orgName: TextView
        var orgImage: ImageView
        var orgLayout : LinearLayout

        init {
            orgName = itemView.findViewById(R.id.orgTitle)
            orgImage = itemView.findViewById(R.id.orgImageView)
            orgLayout = itemView.findViewById(R.id.org_layout)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_main,parent,false)
        return OrganizationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  organizationList.size
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val newList = organizationList[position]
        Log.d("OrganizationList", "Organization: ${newList.name}")
        Log.d("OrganizationList", "Category: ${newList.imageUrl}")
        holder.orgName.text = newList.name
        Picasso.get().load(newList.imageUrl).into(holder.orgImage)

        holder.orgLayout.setOnClickListener {
            val intent = Intent(c, ActivityDonate::class.java)
            // Add any extra data to the intent if needed
            intent.putExtra("costBreakfast", newList.costBreakfast)
            intent.putExtra("costLunch", newList.costLunch)
            intent.putExtra("costDinner", newList.costDinner)
            intent.putExtra("count", newList.count)
            intent.putExtra("organization", newList.name)

            c.startActivity(intent)
        }
    }
}