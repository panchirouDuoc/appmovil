package com.example.appmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmovil.navigation.AppScreen
import com.example.appmovil.ui.theme.AppMovilTheme
import com.example.appmovil.view.*
import com.example.appmovil.viewModel.AuthViewModel
import com.example.appmovil.viewModel.CartViewModel
import com.example.appmovil.viewModel.OrderViewModel


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
    val authViewModel: AuthViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()



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
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
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
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel
            )
        }
        composable(AppScreen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )

        }
        composable(AppScreen.Orders.route) {
            OrdersScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                cartViewModel = cartViewModel
            )
        }
    }
}
    