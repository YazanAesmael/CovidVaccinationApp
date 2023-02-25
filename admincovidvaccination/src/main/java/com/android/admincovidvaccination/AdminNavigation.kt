package com.android.admincovidvaccination

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AdminNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("home_screen") {
            ProfileScreen(navController)
        }
        composable("user_info_screen") {
            UsersLazyColumn(AdminMainViewModel())
        }
        composable("splash_screen") {
            SplashScreen(navController)
        }
    }
}