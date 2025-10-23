package com.example.appmovil.model

// src/com/example/app/model/Producto.kt
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val categoria: String,
    val imagen: String // Ruta de la imagen
)

data class CartItem(
    val producto: Producto,
    val cantidad: Int
)