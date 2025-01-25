package com.example.navigationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigationapp.ui.theme.NavigationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // First, you create the NavController
    val navController = rememberNavController()

    // Then, you define a NavHost and specify a start destination
    NavHost(navController = navController, startDestination = Screen.SignIn.route) {
        composable(Screen.SignIn.route) { SignInScreen(navController) }
        composable("${Screen.Dashboard.route}/{name}") {
            val name = it.arguments?.getString("name") ?: "--"
            DashboardScreen(name)
        }
        composable("${Screen.Home.route}/{name}") {
            HomeScreen(it.arguments)
        }

    }

}