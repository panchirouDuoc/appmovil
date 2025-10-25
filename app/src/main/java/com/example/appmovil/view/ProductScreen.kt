package com.example.appmovil.view

// src/com/example/app/view/ProductsScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmovil.viewmodel.CartViewModel
import com.example.appmovil.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController, cartViewModel: CartViewModel) {
    // Observa el estado (LiveData) de los productos
    val products by cartViewModel.productos.observeAsState(emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Productos Huerto Hogar") }) }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Simula la disposición de 2 productos por fila (Col md={6} en React)
            contentPadding = innerPadding,
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product) {
                    // Acción al pulsar: llama al ViewModel
                    cartViewModel.addProductToCart(product)
                }
            }
        }
    }
}

// Componente individual (simula la Card en Productos.jsx)
@Composable
fun ProductCard(product: Producto, onAdd: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Card.Title
            Text(text = "$${product.precio.toString()}", style = MaterialTheme.typography.titleLarge)

            // Card.Text
            Text(text = product.nombre, style = MaterialTheme.typography.titleMedium)

            // Card.Text - descripción
            Text(text = product.descripcion, style = MaterialTheme.typography.bodySmall, modifier = Modifier.heightIn(max = 60.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // Button - onClick={() => onAdd(p)}
            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
                Text("+ Agregar")
            }
        }
    }
}