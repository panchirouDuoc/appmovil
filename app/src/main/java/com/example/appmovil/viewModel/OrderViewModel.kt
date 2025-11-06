package com.example.appmovil.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovil.model.CartItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import com.example.appmovil.model.Order

enum class OrderStatus(val displayText: String) {
    NONE("Sin pedido"),
    CONFIRMADO("Confirmado"),
    PREPARADO("En preparación"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado")
}

class OrderViewModel : ViewModel() {

    private val _currentOrder = MutableLiveData<Order?>(null)
    val currentOrder: LiveData<Order?> = _currentOrder

    fun createOrder(items: List<CartItem>, total: Int) {
        val newOrder = Order(
            numeroPedido = Random.nextInt(100000, 999999).toString(),
            fechaConfirmacion = System.currentTimeMillis(),
            items = items,
            total = total
        )
        _currentOrder.value = newOrder
    }

    fun advanceOrderStatus() {
        val currentStatus = _currentOrder.value?.status ?: return
        val nextStatus = when (currentStatus) {
            OrderStatus.CONFIRMADO -> OrderStatus.PREPARADO
            OrderStatus.PREPARADO -> OrderStatus.ENVIADO
            OrderStatus.ENVIADO -> OrderStatus.ENTREGADO
            else -> currentStatus
        }

        _currentOrder.value = _currentOrder.value?.copy(status = nextStatus)
    }

    fun clearOrder() {
        _currentOrder.value = null
    }


    fun formatCurrency(value: Int): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            format.maximumFractionDigits = 0
            format.format(value)
        } catch (e: Exception) {
            "$$value"
        }
    }

    fun formatDate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL"))
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            "-"
        }
    }
}
