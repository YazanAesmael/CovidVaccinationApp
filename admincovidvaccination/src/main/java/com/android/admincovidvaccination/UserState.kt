package com.android.admincovidvaccination

open class UserState {
    class Success(val postDetails: MutableList<UserDetails>): UserState()
    class Failure(val message: String): UserState()
    object Loading: UserState()
    object Empty: UserState()
}