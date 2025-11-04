package com.example.appmovil.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmovil.viewmodel.CartViewModel
import com.example.appmovil.model.Producto
import com.example.appmovil.navigation.AppScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.error
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController, cartViewModel: CartViewModel) {

    val products by cartViewModel.productos.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos Huerto Hogar") })
        },
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onAdd = {
                        cartViewModel.addProductToCart(product)

                    }
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: Producto, onAdd: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)){
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "file:///android_asset/${product.imagen}")
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentDescription = "Imagen de ${product.nombre}",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = product.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    modifier = Modifier.heightIn(min = 32.dp)
                )
                Text(
                    text = "$${String.format("%,d", product.precio)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
                    Text("+ Agregar")
                }
            }
        }
    }
}
