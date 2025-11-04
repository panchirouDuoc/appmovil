package com.example.appmovil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovil.model.Producto

data class CartItem(
    val producto: Producto,
    val cantidad: Int
)

class CartViewModel : ViewModel() {
    private val _productos = MutableLiveData(initialProductList())
    val productos: LiveData<List<Producto>> = _productos

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

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

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun calculateTotal(): Int {
        return _cartItems.value.orEmpty().sumOf { it.producto.precio * it.cantidad }
    }

    fun calculatePoints(): Int {
        return (calculateTotal() * 0.02).toInt()
    }
}
private fun initialProductList() = listOf(
    Producto(1, "Manzana Fuji Bolsa, 1 kg", 1890, "Manzanas Fuji crujientes y dulces...", "frutas", "/images/manzana.avif"),
    Producto(2, "Naranja Valencia Pote, 300 g", 2490, "Jugosas y ricas en vitamina C...", "frutas", "/images/naranja.avif"),
    // ... Agrega los demás productos aquí
)