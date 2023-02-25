package com.android.admincovidvaccination

data class UserDetails(
    var name: String = "",
    var state: String = "",
    var age: String = "",
    var vaccine: String = "",
    var dose: String = "",
    var doseDate: String = "",
    var userNumber: String = "",
    var isBooked: String = "false",
)