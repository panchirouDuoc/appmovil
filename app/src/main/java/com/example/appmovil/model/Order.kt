package com.example.appmovil.model
import com.example.appmovil.viewModel.OrderStatus

data class Order(
    val numeroPedido: String,
    val fechaConfirmacion: Long,
    val items: List<CartItem>,
    val total: Int,
    val status: OrderStatus = OrderStatus.CONFIRMADO

)