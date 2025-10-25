package com.example.appmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmovil.navigation.AppScreen // <-- MUY IMPORTANTE
import com.example.appmovil.ui.theme.AppMovilTheme // O el nombre de tu tema
import com.example.appmovil.view.*
import com.example.appmovil.viewmodel.AuthViewModel
import com.example.appmovil.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMovilTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // CREACIÓN ÚNICA DE VIEWMODELS: Este es el único lugar donde se crean.
    val authViewModel: AuthViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    // NAVEGACIÓN ÚNICA: Este es el único NavHost de toda la app.
    NavHost(navController = navController, startDestination = AppScreen.Welcome.route) {

        composable(AppScreen.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppScreen.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppScreen.Register.route) {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppScreen.Main.route) {
            MainScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(AppScreen.Products.route) {
            ProductsScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        composable(AppScreen.Cart.route) {
            CarritoScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        composable(AppScreen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Aquí puedes añadir el resto de tus rutas
        // composable(AppScreen.Products.route) { ... }
        // composable(AppScreen.Cart.route) { ... }
    }
}
    