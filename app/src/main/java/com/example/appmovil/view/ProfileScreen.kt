package com.example.appmovil.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.example.appmovil.viewModel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.appmovil.navigation.AppScreen
import com.example.appmovil.ui.screens.ComponentSection



@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {

    val currentUser by authViewModel.currentUser.observeAsState()
    val isAuthenticated by authViewModel.isAuthenticated.observeAsState()
    var selectedItem by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == false) {
            navController.navigate(AppScreen.Welcome.route) {
                popUpTo(AppScreen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.logout()
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, "Cerrar Sesión")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {

                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
                currentUser?.let { user ->
                ComponentSection(title = "Opciones") {

                    Text(
                        text = "Información del Usuario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Nombre:",
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = user.name)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cuenta creada:",
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date(user.createdAt))
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Green.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ Sesión activa",
                            modifier = Modifier.padding(12.dp),
                            color = Color.Green,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Divider()

                    ListItem(
                        headlineContent = { Text("Configuración") },
                        supportingContent = { Text("Ajustes de la aplicación") },
                        leadingContent = {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                    Divider()

                    ListItem(
                        headlineContent = { Text("Notificaciones") },
                        supportingContent = { Text("Gestionar notificaciones") },
                        leadingContent = {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = false,
                                onCheckedChange = {}
                            )
                        }
                    )
                }
                OutlinedButton(
                    onClick = {
                        authViewModel.deleteUser()
                        navController.navigate(AppScreen.Welcome.route) {
                            popUpTo(AppScreen.Profile.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar Cuenta")
                }
            }}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController(), authViewModel = viewModel())
}
