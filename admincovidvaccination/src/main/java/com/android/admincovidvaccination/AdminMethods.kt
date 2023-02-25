package com.android.admincovidvaccination

import android.content.Context
import android.util.Log.d
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var auth: FirebaseAuth
internal val database = Firebase.database("YOUR FIREBASE DATABASE URL")
const val MY_LOG = "logCompose"

fun updateSlots(context: Context, slots: Int) {
    val ref = database.getReference("Admin/Info/available slots/")
    ref.setValue(slots).addOnSuccessListener {
        Toast.makeText(context, "Slots Updated!\nAvailable Slots: $slots", Toast.LENGTH_SHORT ).show()
    }
}

fun getSlots() {
    val ref = database.getReference("Admin/Info/available slots/")
    ref.get().addOnSuccessListener { slots ->
        val cast = slots.value as Long
        adminSlots = cast.toInt()
        d(MY_LOG, "getSlots(): $adminSlots")

    }

}

