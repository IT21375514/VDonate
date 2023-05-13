package com.example.myproject.model
import android.provider.ContactsContract.CommonDataKinds.Organization
import java.util.Date

data class Donation(var key: String? = null,
                    val category : String? = "",
                    val type : String? = "",
                    val date: String? = "",
                    val cost: Double? = 0.0,
                    var note : String? = "",
                    val items : String? ="",
                    val imageUrl: String? = "",
                    var organization: String? = ""
)

