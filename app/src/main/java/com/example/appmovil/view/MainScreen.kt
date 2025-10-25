package com.example.appmovil.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmovil.R
import com.example.appmovil.navigation.AppScreen
import com.example.appmovil.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, authViewModel: AuthViewModel) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Principal") },
                actions = {
                    // Botón para el menú desplegable (tres puntos)
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    // Menú desplegable
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver Productos") },
                            onClick = {
                                navController.navigate(AppScreen.Products.route)
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mi Carrito") },
                            onClick = {
                                navController.navigate(AppScreen.Cart.route)
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mi Perfil") },
                            onClick = {
                                // TODO: navController.navigate(AppScreen.Profile.route)
                                menuExpanded = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión") },
                            onClick = {
                                authViewModel.logout()
                                navController.navigate(AppScreen.Login.route)
                                navController.navigate(AppScreen.Welcome.route) { popUpTo(AppScreen.Main.route) { inclusive = true } }
                                menuExpanded = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        MainScreenContent(
            modifier = Modifier.padding(innerPadding),
            onProductsClicked = { navController.navigate(AppScreen.Products.route) },
            onCartClicked = { navController.navigate(AppScreen.Cart.route) },
            onProfileClicked = { /* TODO: navController.navigate(AppScreen.Profile.route) */ }
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onProductsClicked: () -> Unit,
    onCartClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo en el centro
        Image(
            painter = painterResource(id = R.drawable.logo), // Reemplaza 'logo' con el nombre de tu imagen
            contentDescription = "Logo de la tienda",
            modifier = Modifier
                .size(500.dp)
                .padding(bottom = 48.dp)
        )

        // 2. Botones de navegación
        Button(
            onClick = onProductsClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Ver Productos", style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = onCartClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Mi Carrito", style = MaterialTheme.typography.titleMedium)
        }

        OutlinedButton(
            onClick = onProfileClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Mi Perfil", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = rememberNavController(),
        authViewModel = viewModel()
    )
}

