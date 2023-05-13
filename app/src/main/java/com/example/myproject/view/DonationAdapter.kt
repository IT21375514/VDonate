package com.example.myproject.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.model.Donation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DonationAdapter(private val c: Context,private val donationList:ArrayList<Donation>):RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    private lateinit var dbref : DatabaseReference

    inner class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var mbNum:TextView
        private var mMenus: ImageView

        init {
            name = itemView.findViewById(R.id.mTitle)
            mbNum = itemView.findViewById(R.id.mSubTitle)
            mMenus = itemView.findViewById(R.id.mMenus)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        @SuppressLint("DiscouragedPrivateApi")
        private fun popupMenus(v: View) {
            dbref = FirebaseDatabase.getInstance().getReference("/Realtime Database/")
            val position = donationList[adapterPosition]
            val activityContext = v.context
            val popupMenus = PopupMenu(activityContext, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(c).inflate(R.layout.update_donation,null)
                        val note = v.findViewById<EditText>(R.id.updateNote)
                        AlertDialog.Builder(activityContext)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                val donationRef = dbref.child(position.key!!)
                                donationRef.child("donations").setValue(note.toString())
                                    .addOnSuccessListener {
                                        position.note=note.text.toString()
                                        notifyDataSetChanged()
                                        Toast.makeText(c, "User Information is Edited", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                    }

                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(activityContext)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                val donation = donationList[adapterPosition]

                                // Retrieve the key from the Donation object
                                val donationKey = donation.key
                                Log.d("DonationID", donationKey.toString())

                                // Remove the item from Firebase
                                val donationsRef = dbref.child("donations")
                                if (donationKey != null) {
                                    donationsRef.child(donationKey).removeValue()
                                }

                                donationList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c, "Deleted this Information", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_donation,parent,false)
        return DonationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  donationList.size
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val newList = donationList[position]
        Log.d("DonationList", "Organization: ${newList.organization}")
        Log.d("DonationList", "Category: ${newList.category}")
        holder.name.text = newList.organization
        holder.mbNum.text = newList.category
    }
}