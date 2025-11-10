package com.example.appinsumos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appinsumos.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel
) {
    val usuario by viewModel.usuario.observeAsState()
    val modoEdicion by viewModel.modoEdicion.observeAsState(false)
    val mostrarDialogoCerrarSesion by viewModel.mostrarDialogoCerrarSesion.observeAsState(false)
    val mensaje by viewModel.mensaje.observeAsState(null)

    // Estados locales para edición
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var rut by remember { mutableStateOf(usuario?.rut ?: "") }
    var email by remember { mutableStateOf(usuario?.email ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }
    var direccion by remember { mutableStateOf(usuario?.direccion ?: "") }

    // Actualizar campos cuando cambie el usuario
    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            rut = it.rut
            email = it.email
            telefono = it.telefono
            direccion = it.direccion
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleModoEdicion() }) {
                        Icon(
                            if (modoEdicion) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (modoEdicion) "Cancelar" else "Editar"
                        )
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (it.contains("exitosamente")) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null
                        )
                        Text(it, fontSize = 14.sp)
                    }
                }
            }

            // Avatar y nombre
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Text(
                        text = usuario?.nombre ?: "Cargando...",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    AssistChip(
                        onClick = {},
                        label = { Text("Paciente Activo") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )
                }
            }

            // Información personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    CampoInfo(
                        icono = Icons.Default.Person,
                        label = "Nombre Completo",
                        valor = nombre,
                        editable = modoEdicion,
                        onValueChange = { nombre = it }
                    )

                    CampoInfo(
                        icono = Icons.Default.AccountBox,
                        label = "RUT",
                        valor = rut,
                        editable = modoEdicion,
                        onValueChange = { rut = it }
                    )

                    CampoInfo(
                        icono = Icons.Default.Email,
                        label = "Correo Electrónico",
                        valor = email,
                        editable = modoEdicion,
                        onValueChange = { email = it }
                    )

                    CampoInfo(
                        icono = Icons.Default.Phone,
                        label = "Teléfono",
                        valor = telefono,
                        editable = modoEdicion,
                        onValueChange = { telefono = it }
                    )

                    CampoInfo(
                        icono = Icons.Default.LocationOn,
                        label = "Dirección de Entrega",
                        valor = direccion,
                        editable = modoEdicion,
                        onValueChange = { direccion = it },
                        multilinea = true
                    )
                }
            }

            // Botón guardar cambios (solo visible en modo edición)
            if (modoEdicion) {
                Button(
                    onClick = {
                        usuario?.let {
                            viewModel.actualizarUsuario(
                                it.copy(
                                    nombre = nombre,
                                    rut = rut,
                                    email = email,
                                    telefono = telefono,
                                    direccion = direccion
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Opciones adicionales
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    OpcionMenu(
                        icono = Icons.Default.Notifications,
                        texto = "Notificaciones",
                        onClick = { /* Acción futura */ }
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    OpcionMenu(
                        icono = Icons.Default.Settings,
                        texto = "Configuración",
                        onClick = { /* Acción futura */ }
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    OpcionMenu(
                        icono = Icons.Default.Info,
                        texto = "Ayuda y Soporte",
                        onClick = { /* Acción futura */ }
                    )
                }
            }

            // Botón cerrar sesión
            OutlinedButton(
                onClick = { viewModel.mostrarDialogoCerrarSesion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
                )
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Diálogo de confirmación para cerrar sesión
        if (mostrarDialogoCerrarSesion) {
            AlertDialog(
                onDismissRequest = { viewModel.ocultarDialogoCerrarSesion() },
                icon = {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Cerrar Sesión", fontWeight = FontWeight.Bold) },
                text = { Text("¿Está seguro que desea cerrar sesión?") },
                confirmButton = {
                    Button(
                        onClick = { viewModel.cerrarSesion() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cerrar Sesión")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.ocultarDialogoCerrarSesion() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun CampoInfo(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    valor: String,
    editable: Boolean,
    onValueChange: (String) -> Unit,
    multilinea: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icono,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (editable) {
            OutlinedTextField(
                value = valor,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multilinea,
                maxLines = if (multilinea) 3 else 1,
                colors = OutlinedTextFieldDefaults.colors()
            )
        } else {
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun OpcionMenu(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = texto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}