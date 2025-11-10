package com.example.appinsumos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appinsumos.viewmodel.InsumosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitarInsumosScreen(
    navController: NavController,
    viewModel: InsumosViewModel
) {
    val insumoSeleccionado by viewModel.insumoSeleccionado.observeAsState("")
    val cantidad by viewModel.cantidad.observeAsState("")
    val prioridad by viewModel.prioridad.observeAsState("Programada")
    val mostrarConfirmacion by viewModel.mostrarConfirmacion.observeAsState(false)
    val mensaje by viewModel.mensaje.observeAsState(null)

    var expandedInsumo by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitar Insumos", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Complete el formulario",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Mensaje de estado (si existe)
            mensaje?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (it.contains("exitosamente"))
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            if (it.contains("exitosamente")) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null
                        )
                        Text(it)
                    }
                }
            }

            // Selector de Insumo
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tipo de Insumo",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedInsumo,
                        onExpandedChange = { expandedInsumo = it }
                    ) {
                        OutlinedTextField(
                            value = insumoSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedInsumo) },
                            placeholder = { Text("Seleccione un insumo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedInsumo,
                            onDismissRequest = { expandedInsumo = false }
                        ) {
                            viewModel.insumos.forEach { insumo ->
                                DropdownMenuItem(
                                    text = { Text(insumo) },
                                    onClick = {
                                        viewModel.actualizarInsumo(insumo)
                                        expandedInsumo = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Campo de Cantidad
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cantidad",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { viewModel.actualizarCantidad(it) },
                        placeholder = { Text("Ingrese la cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Add, contentDescription = "Cantidad")
                        }
                    )
                }
            }

            // Selector de Prioridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Prioridad",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = it }
                    ) {
                        OutlinedTextField(
                            value = prioridad,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPrioridad) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            leadingIcon = {
                                Icon(
                                    if (prioridad == "Urgente") Icons.Default.Warning else Icons.Default.DateRange,
                                    contentDescription = "Prioridad",
                                    tint = if (prioridad == "Urgente")
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            viewModel.prioridades.forEach { prioridadItem ->
                                DropdownMenuItem(
                                    text = { Text(prioridadItem) },
                                    onClick = {
                                        viewModel.actualizarPrioridad(prioridadItem)
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Enviar
            Button(
                onClick = { viewModel.enviarSolicitud() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = insumoSeleccionado.isNotEmpty() && cantidad.isNotEmpty()
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar Solicitud", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Dialog de confirmación
        if (mostrarConfirmacion) {
            AlertDialog(
                onDismissRequest = { viewModel.ocultarConfirmacion() },
                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                title = { Text("Solicitud Enviada", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Su solicitud ha sido registrada exitosamente:")
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("• Insumo: $insumoSeleccionado", fontWeight = FontWeight.Medium)
                        Text("• Cantidad: $cantidad", fontWeight = FontWeight.Medium)
                        Text("• Prioridad: $prioridad", fontWeight = FontWeight.Medium)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.ocultarConfirmacion()
                            viewModel.limpiarFormulario()
                            navController.popBackStack()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}