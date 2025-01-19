package com.example.navigationsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "first_screen"
    ) {
        composable(route = "first_screen") {
            // Step 3
            FirstScreen({ name ->
                navController.navigate("second_screen/$name")
            })
        }
        composable(route = "second_screen/{name}") { // Step 4
            // step 5
            val name = it.arguments?.getString("name") ?: "--"
            SecondScreen(name) {
                navController.navigate("third_screen")
            }
        }
        composable(route = "third_screen") {
            ThirdScreen {
                navController.navigate("first_screen")
            }
        }
    }
}