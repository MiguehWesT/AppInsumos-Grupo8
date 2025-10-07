package com.example.appinsumos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appinsumos.navigation.Screen

data class Pedido(
    val id: String,
    val insumo: String,
    val cantidad: String,
    val estado: EstadoPedido,
    val fecha: String,
    val prioridad: String
)

enum class EstadoPedido(val texto: String, val emoji: String, val color: Color) {
    PENDIENTE("Pendiente", "⏳", Color(0xFFFFA726)),
    EN_PREPARACION("En preparación", "🏭", Color(0xFF42A5F5)),
    EN_REPARTO("En reparto", "🚚", Color(0xFF9C27B0)),
    ENTREGADO("Entregado", "✅", Color(0xFF66BB6A))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoPedidoScreen(navController: NavController) {
    // insumos simulados
    val pedidos = remember {
        listOf(
            Pedido("001", "Insulina", "10 unidades", EstadoPedido.EN_REPARTO, "05/10/2025", "Urgente"),
            Pedido("002", "Agujas", "50 unidades", EstadoPedido.EN_PREPARACION, "04/10/2025", "Programada"),
            Pedido("003", "Gasas", "20 paquetes", EstadoPedido.ENTREGADO, "01/10/2025", "Programada"),
            Pedido("004", "Jeringas", "30 unidades", EstadoPedido.PENDIENTE, "06/10/2025", "Urgente")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento de Pedidos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.SolicitarInsumos.route) },
                icon = { Icon(Icons.Default.Add, "Nuevo") },
                text = { Text("Nueva Solicitud") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        if (pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        "No hay pedidos registrados",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        onClick = {
                            navController.navigate(Screen.DetallePedido.createRoute(pedido.id))
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pedido.insumo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Pedido #${pedido.id}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (pedido.prioridad == "Urgente") {
                    AssistChip(
                        onClick = {},
                        label = { Text("Urgente", fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            labelColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pedido.fecha,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cantidad: ${pedido.cantidad}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = pedido.estado.color.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = pedido.estado.emoji,
                            fontSize = 16.sp
                        )
                        Text(
                            text = pedido.estado.texto,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = pedido.estado.color
                        )
                    }
                }
            }
        }
    }
}