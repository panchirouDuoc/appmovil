package com.example.appmovil.viewModel

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmovil.model.CartItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import com.example.appmovil.model.Order
import com.example.appmovil.R


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

    fun advanceOrderStatus(context: Context) {
        _currentOrder.value?.let { order ->
            val nextStatus = when (order.status) {
                OrderStatus.CONFIRMADO -> OrderStatus.PREPARADO
                OrderStatus.PREPARADO -> OrderStatus.ENVIADO
                OrderStatus.ENVIADO -> OrderStatus.ENTREGADO
                else -> order.status
            }

            if (nextStatus != order.status) {
                _currentOrder.value = order.copy(status = nextStatus)
                if (nextStatus == OrderStatus.ENVIADO || nextStatus == OrderStatus.ENTREGADO) {
                    val notificationText = if (nextStatus == OrderStatus.ENVIADO) {
                        "¡Tu pedido ha sido enviado!"
                    } else { "¡Tu pedido ha sido entregado!" }
                    sendOrderStatusNotification(context, notificationText)
                }
            }
        }
    }

    private fun sendOrderStatusNotification(context: Context, text: String) {
        val CHANNEL_ID = "order_status_channel"
        val NOTIFICATION_ID = 1

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // .setContentTitle("Actualización de tu Pedido")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
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
