package com.example.appmovil.model
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val categoria: String,
    val imagen: String
)

data class CartItem(
    val producto: Producto,
    val cantidad: Int
)