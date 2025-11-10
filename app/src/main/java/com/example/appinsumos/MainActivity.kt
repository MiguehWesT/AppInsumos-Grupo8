package com.example.appinsumos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.appinsumos.data.PedidoRepository
import com.example.appinsumos.data.UsuarioRepository
import com.example.appinsumos.navigation.NavGraph
import com.example.appinsumos.ui.theme.AppInsumosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar repositorios globalmente
        val pedidoRepository = PedidoRepository(this)
        val usuarioRepository = UsuarioRepository(this)

        setContent {
            AppInsumosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        pedidoRepository = pedidoRepository,
                        usuarioRepository = usuarioRepository
                    )
                }
            }
        }
    }
}