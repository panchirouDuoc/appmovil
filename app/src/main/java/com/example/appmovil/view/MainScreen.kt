package com.example.appmovil.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

                }
            )
        },

        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            //contenido principal
            item {
                MainScreenContent(
                    onProductsClicked = { navController.navigate(AppScreen.Products.route) },
                    onCartClicked = { navController.navigate(AppScreen.Cart.route) },
                    onProfileClicked = { navController.navigate(AppScreen.Profile.route) }
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreen.Main.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreen.Products.route) },
            icon = { Icon(Icons.Default.AttachMoney, contentDescription = "Productos") },
            label = { Text("Productos") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreen.Cart.route) },
            icon = {
                BadgedBox(badge = { Badge { Text("") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            },
            label = { Text("Carrito") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* navController.navigate(AppScreen.Orders.route) */ },
            icon = { Icon(Icons.Default.Textsms, contentDescription = "Pedidos") },
            label = { Text("Pedidos") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreen.Profile.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
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
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.descuentos),
            contentDescription = "descuentos",
            modifier = Modifier
                .size(400.dp),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.envios),
            contentDescription = "envios",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 10.dp),
            contentScale = ContentScale.Fit
        )
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

