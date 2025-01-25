package com.example.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SignInScreen(navController: NavController) {
    val name = "rakesh"
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "Sign In")
        Button(onClick = { navController.navigate("${Screen.Dashboard.route}/$name") }) {
            Text(text = "Sign In")
        }

        Button(onClick = { navController.navigate("${Screen.Home.route}/$name") }) {
            Text(text = "Home")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    val navController = rememberNavController()
    SignInScreen(navController)
}