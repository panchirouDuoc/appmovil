package com.example.appmovil.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmovil.viewmodel.AuthViewModel

// 1. Define las rutas (AppScreen) - (Similar a definir constantes de rutas en React)
sealed class AppScreen(val route: String){
    object Welcome : AppScreen("welcome")
    object Register : AppScreen("register")
    object Login : AppScreen("login")
    object QuickLogin : AppScreen("quick_login")
    object Profile : AppScreen("profile")
    object Products : AppScreen("products") // Añadimos la ruta de productos
    object Cart : AppScreen("cart")         // Añadimos la ruta del carrito
    // ... otras rutas
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val hasUser = authViewModel.hasExistingUser()
    val startDestination = if (hasUser) AppScreen.QuickLogin.route else AppScreen.Welcome.route // Cambio a Welcome

    NavHost(navController = navController, startDestination = startDestination) {

        // 2. Conecta cada ruta a un Composable (similar a <Route path="/login" element={<Login />} />)
        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = AppScreen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = AppScreen.Register.route) {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = AppScreen.Profile.route) {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        // Añadimos las rutas de E-commerce
        composable(route = AppScreen.Products.route) {
            ProductsScreen(navController = navController)
        }

        composable(route = AppScreen.Cart.route) {
            CarritoScreen(navController = navController)
        }
    }
}