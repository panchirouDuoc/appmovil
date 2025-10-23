package com.example.appmovil.model

data class User(
    val name: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)