package com.example.navigationapp

sealed class Screen(val route: String) {
    data object SignIn : Screen("signIn")
    data object Dashboard : Screen("dashboard")
    data object Home : Screen("home")
    data object Setting : Screen("setting")
}