package com.android.covidvaccination

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainViewModel: ViewModel() {
    init {
        fetchBloodDataFromFirebase()
    }

    private fun fetchBloodDataFromFirebase() {
        val database = Firebase.database("https://vaccination-95807-default-rtdb.asia-southeast1.firebasedatabase.app")
        val uid = FirebaseAuth.getInstance().uid.toString()
        auth = FirebaseAuth.getInstance()

        database.getReference("Users/").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userDetails.clear()
                    for (dataSnap in snapshot.children) {
                        val userUid = dataSnap.key.toString()

                        fun getName() {
                            database.getReference("Users/$userUid/name/")
                                .get().addOnSuccessListener {
                                    userName = it.value.toString()
                                }
                        }
                        fun getState() {
                            database.getReference("Users/$userUid/state/")
                                .get().addOnSuccessListener {
                                userState = it.value.toString()
                            }
                        }
                        fun getAge() {
                            database.getReference("Users/$userUid/age/")
                                .get().addOnSuccessListener {
                                userAge = it.value.toString()
                            }
                        }
                        fun getVaccine() {
                            database.getReference("Users/$userUid/vaccine/")
                                .get().addOnSuccessListener {
                                userVaccine = it.value.toString()
                            }
                        }
                        fun getDoses() {
                            database.getReference("Users/$userUid/doses/")
                                .get().addOnSuccessListener {
                                userDoses = it.value.toString()
                            }
                        }
                        fun getDate() {
                            database.getReference("Users/$userUid/date/")
                                .get().addOnSuccessListener {
                                userDoseDate = it.value.toString()
                            }
                        }
                        fun getNumber() {
                            userNumber = auth.currentUser?.phoneNumber.toString()
                        }
                        fun getSlots() {
                            val ref = database.getReference("Admin/Info/available slots/")
                            ref.get().addOnSuccessListener { slots ->
                                val cast = slots.value
                                adminSlots = cast.toString()
                                Log.d(MY_LOG, "getSlots(): $adminSlots")
                            }
                        }

                        getSlots()
                        getName()
                        getState()
                        getAge()
                        getVaccine()
                        getDoses()
                        getDate()
                        getNumber()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}