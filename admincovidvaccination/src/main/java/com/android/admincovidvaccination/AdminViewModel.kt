package com.android.admincovidvaccination

import android.util.Log.d
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminMainViewModel: ViewModel() {
    val response: MutableState<UserState> = mutableStateOf(UserState.Empty)
    init {
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        response.value = UserState.Loading
        val database = Firebase.database("https://vaccination-95807-default-rtdb.asia-southeast1.firebasedatabase.app")

        val ref = database.getReference("Admin/Info/available slots/")


        database.getReference("Users/").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    ref.addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val slots = snapshot.value as Long
                                adminSlots = slots.toInt()
                                d(MY_LOG, "admin slots: $adminSlots")
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        }
                    )

                    userDetails.clear()
                    for (dataSnap in snapshot.children) {
                        val userUid = dataSnap.key

                        fun getName() {
                            database.getReference("Users/$userUid/name/")
                                .get().addOnSuccessListener {
                                    userName = it.value.toString()
//                                                d(MY_LOG, "name: $userName")
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
                            database.getReference("Users/$userUid/number/")
                                .get().addOnSuccessListener {
                                    userNumber = it.value.toString()
                                }
                        }
                        fun getBooked() {
                            database.getReference("Users/$userUid/isBooked/")
                                .get().addOnSuccessListener {
                                    userIsBooked = it.value.toString()
                                }
                        }

                        getName()
                        getState()
                        getAge()
                        getVaccine()
                        getDoses()
                        getDate()
                        getNumber()
                        getBooked()

                        database.getReference("Users/$userUid") .addValueEventListener(
                            object  : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.key
                                    user?.forEach { _ ->
                                        val users = UserDetails(userName, userState, userAge, userVaccine, userDoses, userDoseDate, userNumber, userIsBooked)
                                        response.value = UserState.Success(userDetails)
                                        if (userDetails.contains(users)) return
                                        userDetails.add(users)
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            }
                        )
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}