package com.example.appmovil.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmovil.model.CartItem
import com.example.appmovil.model.Producto
import com.example.appmovil.navigation.AppScreen

import com.example.appmovil.viewModel.CartViewModel
import com.example.appmovil.viewModel.OrderViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, cartViewModel: CartViewModel, orderViewModel: OrderViewModel) {

    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
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

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemRow(item = item)
                    }
                }

                CartSummary(total = total, puntos = puntos)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { cartViewModel.clearCart() }) {
                        Text("Vaciar carrito")
                    }
                    Button(onClick = {
                        orderViewModel.createOrder(cartItems, total)
                        cartViewModel.clearCart()
                        navController.navigate(AppScreen.Orders.route)
                    }) {
                        Text("Pagar")
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
