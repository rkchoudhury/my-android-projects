package com.example.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun SettingScreen(user: User) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Name: ${user.name}")
        Text(text = "Age: ${user.age}")
        Text(text = "DOB: ${user.dob}")
    }
}

@Composable
fun SettingScreen2(navController: NavController) {
    val user =
        navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
            ?: User("", 1, "")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Name: ${user.name}")
        Text(text = "Age: ${user.age}")
        Text(text = "DOB: ${user.dob}")
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    val user: User = User("rakesh", 28, "31/05/1996")
    SettingScreen(user)
}