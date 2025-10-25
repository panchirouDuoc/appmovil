package com.example.appmovil.navigation
sealed class AppScreen(val route: String) {
    object Welcome : AppScreen("welcome_screen")
    object Login : AppScreen("login_screen")
    object Register : AppScreen("register_screen")
    object Main : AppScreen("main_screen")
    object Products : AppScreen("products_screen")
    object Cart : AppScreen("cart_screen")
    object Profile : AppScreen("profile_screen")
}
