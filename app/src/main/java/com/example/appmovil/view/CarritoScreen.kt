package com.example.appmovil.view

// src/com/example/app/view/CarritoScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmovil.viewmodel.CartViewModel
import com.example.appmovil.viewmodel.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    // Observa el estado del carrito
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())

    // Obtiene los cálculos (similar a useMemo)
    val total = cartViewModel.calculateTotal()
    val puntos = cartViewModel.calculatePoints()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall)
            } else {
                // Muestra la lista de productos (similar a Table body en Carrito.jsx)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemRow(item = item)
                    }
                }

                // Resumen del carrito (similar a Table footer en Carrito.jsx)
                CartSummary(total = total, puntos = puntos)

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acción
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { cartViewModel.clearCart() }) {
                        Text("Vaciar carrito") // Similar a onClear
                    }
                    Button(onClick = { /* Lógica de Pagar/Checkout */ }) {
                        Text("Pagar") // Similar a onCheckout
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${item.producto.nombre} x${item.cantidad}", modifier = Modifier.weight(1f))
        Text(text = "$${(item.producto.precio * item.cantidad)}", modifier = Modifier.width(80.dp))
    }
    Divider()
}

@Composable
fun CartSummary(total: Int, puntos: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total carrito:", style = MaterialTheme.typography.titleMedium)
            Text("$${total}", style = MaterialTheme.typography.titleMedium)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Puntos acumulados:", style = MaterialTheme.typography.bodyMedium)
            Text("${puntos}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
