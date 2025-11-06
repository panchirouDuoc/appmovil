package com.example.appmovil.view

import android.Manifest
import android.content.Context
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.example.appmovil.model.Order
import com.example.appmovil.viewModel.OrderViewModel
import com.example.appmovil.viewModel.OrderStatus
import com.example.appmovil.viewModel.CartViewModel
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.appmovil.R
import androidx.compose.runtime.*


private const val CHANNEL_ID = "order_status_channel"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController, orderViewModel: OrderViewModel, cartViewModel: CartViewModel) {
    val order by orderViewModel.currentOrder.observeAsState()
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else { mutableStateOf(true)}
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seguimiento de Pedido") })
        },
        bottomBar = {
            AppBottomNavigationBar(navController = navController, cartViewModel = cartViewModel)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Se requiere permiso de notificación para avisarte sobre tu pedido.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
                            Text("Otorgar Permiso")
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (order == null) {
                        NoOrderView()
                    } else {
                        OrderDetailsView(order = order!!, viewModel = orderViewModel, hasNotificationPermission = hasNotificationPermission)
                    }
                }
            }
        }
    }
}

private fun sendSimpleNotification(context: Context) {

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Pedido Actualizado")
        .setContentText("El estado de tu pedido ha cambiado.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()


    try {
        NotificationManagerCompat.from(context).notify(1, notification)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

@Composable
fun NoOrderView() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No hay ningún pedido activo", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Confirma un pedido desde tu carrito para hacer el seguimiento.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun OrderDetailsView(order: Order, viewModel: OrderViewModel,hasNotificationPermission: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        BoletaCard(order = order, viewModel = viewModel)
        EstadoPedidoCard(orderStatus = order.status)
        OrderActionButtons(
            status = order.status,
            onAdvance = { viewModel.advanceOrderStatus() },
            onFinish = { viewModel.clearOrder() }
        )
    }
}

@Composable
fun BoletaCard(order: Order, viewModel: OrderViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Boleta", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            BoletaInfoRow("N° Pedido:", order.numeroPedido)
            BoletaInfoRow("Fecha:", viewModel.formatDate(order.fechaConfirmacion))
            BoletaInfoRow("Estado:", order.status.displayText)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Productos:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            order.items.forEach { item ->
                Text("${item.producto.nombre} x${item.cantidad}")
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                "Total: ${viewModel.formatCurrency(order.total)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EstadoPedidoCard(orderStatus: OrderStatus) {
    val progressMap = mapOf(
        OrderStatus.NONE to 0.0f,
        OrderStatus.CONFIRMADO to 0.25f,
        OrderStatus.PREPARADO to 0.50f,
        OrderStatus.ENVIADO to 0.75f,
        OrderStatus.ENTREGADO to 1.0f
    )
    val progress = progressMap[orderStatus] ?: 0.0f

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Estado del Pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
            if (orderStatus != OrderStatus.ENTREGADO) {

            }
            Spacer(Modifier.height(8.dp))
            Text(orderStatus.displayText, modifier = Modifier.align(Alignment.End))
        }
    }
}
@Composable
fun OrderActionButtons(status: OrderStatus, onAdvance: () -> Unit, onFinish: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        when (status) {
            OrderStatus.CONFIRMADO -> Button(onClick = onAdvance) { Text("Preparar Pedido") }
            OrderStatus.PREPARADO -> Button(onClick = onAdvance, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000))) { Text("Enviar Pedido") }
            OrderStatus.ENVIADO -> Button(onClick = onAdvance, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))) { Text("Entregar Pedido") }
            OrderStatus.ENTREGADO -> Button(onClick = onFinish, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) { Text("Finalizar (Limpiar)") }
            OrderStatus.NONE -> {} // No muestra botones si no hay pedido
        }
    }
}

@Composable
fun BoletaInfoRow(label: String, value: String) {
    Row {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(value)
    }
}
