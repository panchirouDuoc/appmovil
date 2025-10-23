package com.example.appmovil.viewmodel

// Archivo sugerido: viewmodel/CartViewModel.kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovil.model.Producto // Necesitas crear esta Data Class

// Clase de datos para un producto (basada en tu listaProductos de Productos.jsx)
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int, // Usamos Int para simplificar
    val descripcion: String,
    val categoria: String,
    val imagen: String // Opcional, para usar con Coil/Glide si se necesita
)

// Clase para el ítem del carrito (con cantidad)
data class CartItem(
    val producto: Producto,
    val cantidad: Int
)

class CartViewModel : ViewModel() {

    // 1. Estado de la lista de productos (similar a listaProductos de React)
    private val _productos = MutableLiveData(initialProductList())
    val productos: LiveData<List<Producto>> = _productos

    // 2. Estado del carrito (similar a items en Carrito.jsx)
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // Función para añadir un producto (similar a onAdd en Productos.jsx)
    fun addProductToCart(producto: Producto) {
        val currentItems = _cartItems.value.orEmpty().toMutableList()
        val existingItem = currentItems.find { it.producto.id == producto.id }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(cantidad = existingItem.cantidad + 1)
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = updatedItem
        } else {
            currentItems.add(CartItem(producto, 1))
        }

        _cartItems.value = currentItems
    }

    // Función para vaciar el carrito (similar a onClear en Carrito.jsx)
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Funciones para calcular el total y puntos (similar a useMemo en Carrito.jsx)
    fun calculateTotal(): Int {
        return _cartItems.value.orEmpty().sumOf { it.producto.precio * it.cantidad }
    }

    fun calculatePoints(): Int {
        return (calculateTotal() * 0.02).toInt() // 2% de puntos
    }
}

// Datos iniciales (Copia de listaProductos de Productos.jsx)
private fun initialProductList() = listOf(
    Producto(1, "Manzana Fuji Bolsa, 1 kg", 1890, "Manzanas Fuji crujientes y dulces...", "frutas", "/images/manzana.avif"),
    Producto(2, "Naranja Valencia Pote, 300 g", 2490, "Jugosas y ricas en vitamina C...", "frutas", "/images/naranja.avif"),
    // ... Agrega los demás productos aquí
)