package com.example.appinsumos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoScreen(pedidoId: String, navController: NavController) {
    // datos basados en el ID
    val pedido = Pedido(
        id = pedidoId,
        insumo = "Insulina",
        cantidad = "10 unidades",
        estado = EstadoPedido.EN_REPARTO,
        fecha = "05/10/2025",
        prioridad = "Urgente"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Estado principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = pedido.estado.color.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pedido.estado.emoji,
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pedido.estado.texto,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = pedido.estado.color
                    )
                    Text(
                        text = "Pedido #${pedido.id}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Información del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del Pedido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    InfoRow(
                        icon = Icons.Default.ShoppingCart,
                        label = "Insumo",
                        value = pedido.insumo
                    )

                    InfoRow(
                        icon = Icons.Default.Add,
                        label = "Cantidad",
                        value = pedido.cantidad
                    )

                    InfoRow(
                        icon = Icons.Default.DateRange,
                        label = "Fecha de solicitud",
                        value = pedido.fecha
                    )

                    InfoRow(
                        icon = if (pedido.prioridad == "Urgente") Icons.Default.Warning else Icons.Default.CheckCircle,
                        label = "Prioridad",
                        value = pedido.prioridad,
                        valueColor = if (pedido.prioridad == "Urgente")
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            // timeline estados
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Historial",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TimelineItem(
                        estado = EstadoPedido.PENDIENTE,
                        completado = true,
                        fecha = "05/10/2025 - 08:30"
                    )

                    TimelineItem(
                        estado = EstadoPedido.EN_PREPARACION,
                        completado = true,
                        fecha = "05/10/2025 - 10:15"
                    )

                    TimelineItem(
                        estado = EstadoPedido.EN_REPARTO,
                        completado = true,
                        fecha = "05/10/2025 - 14:20",
                        actual = true
                    )

                    TimelineItem(
                        estado = EstadoPedido.ENTREGADO,
                        completado = false,
                        fecha = "Estimado: 06/10/2025"
                    )
                }
            }

            // Mapa simulado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Mapa en desarrollo",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Aquí se mostrará la ubicación del repartidor",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
fun TimelineItem(
    estado: EstadoPedido,
    completado: Boolean,
    fecha: String,
    actual: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (completado) Icons.Default.CheckCircle else Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (actual) estado.color else if (completado) estado.color.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline
            )
            if (!actual || !completado) {
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .height(24.dp),
                    color = if (completado) estado.color.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = estado.texto,
                fontSize = 15.sp,
                fontWeight = if (actual) FontWeight.Bold else FontWeight.Medium,
                color = if (completado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = fecha,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}