package com.example.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun ProfileScreen(navController: NavController) {
    // Can't access the user data in Profile screen using previousBackStackEntry
    // As no user data is being passed from Settings to Profile screen.
    val user =
        navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
            ?: User("-", 1, "-")
    println("rkkk $user")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile")
        Text(text = "Name: ${user.name}")
        Text(text = "Age: ${user.age}")
        Text(text = "DOB: ${user.dob}")
    }
}
