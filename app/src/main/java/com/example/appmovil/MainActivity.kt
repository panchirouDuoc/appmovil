package com.example.appmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appmovil.ui.theme.AppMovilTheme // Necesitas crear esta carpeta/archivos
import com.example.appmovil.view.AppNavigation // Necesitas crear esta carpeta/archivo (CREADO)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMovilTheme { // Aplica el tema Material 3
                AppNavigation() // Inicia la navegación principal
            }
        }
    }
}