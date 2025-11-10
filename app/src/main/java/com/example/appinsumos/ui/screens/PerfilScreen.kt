package com.example.appinsumos.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.appinsumos.viewmodel.PerfilViewModel
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel
) {
    val context = LocalContext.current
    val usuario by viewModel.usuario.observeAsState()
    val modoEdicion by viewModel.modoEdicion.observeAsState(false)
    val mostrarDialogoCerrarSesion by viewModel.mostrarDialogoCerrarSesion.observeAsState(false)
    val mensaje by viewModel.mensaje.observeAsState(null)

    // Estados locales editables
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var rut by remember { mutableStateOf(usuario?.rut ?: "") }
    var email by remember { mutableStateOf(usuario?.email ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }
    var direccion by remember { mutableStateOf(usuario?.direccion ?: "") }

    // Estados de cámara y GPS
    var imageUri by remember { mutableStateOf(usuario?.fotoUri?.let { Uri.parse(it) }) }
    var locationText by remember { mutableStateOf(usuario?.ubicacion ?: "Ubicación no detectada") }

    // Actualiza los campos si cambia el usuario
    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            rut = it.rut
            email = it.email
            telefono = it.telefono
            direccion = it.direccion
            if (it.fotoUri != null) imageUri = Uri.parse(it.fotoUri)
            if (it.ubicacion != null) locationText = it.ubicacion!!
        }
    }

    // 📸 Lanzador de cámara con manejo seguro de null
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            try {
                val uri = saveBitmapToInternalStorage(context, bitmap)
                imageUri = uri
                viewModel.guardarFotoPerfil(uri.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                viewModel.mostrarMensaje("Error al guardar la foto: ${e.localizedMessage}")
            }
        } else {
            viewModel.mostrarMensaje("⚠️ No se capturó ninguna imagen.")
        }

    }

    // 📍 Lanzador de permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locationText = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    viewModel.guardarUbicacion(locationText)
                } else {
                    locationText = "No se pudo obtener ubicación"
                }
            }
        } else {
            locationText = "Permiso de ubicación denegado"
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

            // ✅ Mensajes
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

            // 🧍 Avatar + cámara
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
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (imageUri != null) {
                                val bitmap = BitmapFactory.decodeStream(
                                    context.contentResolver.openInputStream(imageUri!!)
                                )
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(70.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    Button(onClick = { cameraLauncher.launch(null) }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tomar foto de perfil")
                    }


                    // 📍 Botón de ubicación con permiso automático
                    OutlinedButton(onClick = {
                        when (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )) {
                            PackageManager.PERMISSION_GRANTED -> {
                                val fusedLocationClient =
                                    LocationServices.getFusedLocationProviderClient(context)
                                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                                    if (location != null) {
                                        locationText =
                                            "Lat: ${location.latitude}, Lon: ${location.longitude}"
                                        viewModel.guardarUbicacion(locationText)
                                    } else {
                                        locationText = "No se pudo obtener ubicación"
                                    }
                                }
                            }

                            else -> {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Obtener ubicación")
                    }

                    Text(
                        text = locationText,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // 🧾 Información personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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

            // 💾 Guardar cambios
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
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // 🚪 Cerrar sesión
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
        }

        // ⚠️ Diálogo cerrar sesión
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

// 🔧 Guardar imagen localmente
fun saveBitmapToInternalStorage(context: android.content.Context, bitmap: Bitmap): Uri {
    val filename = "perfil_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, filename)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return Uri.fromFile(file)
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (editable) {
            OutlinedTextField(
                value = valor,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multilinea,
                maxLines = if (multilinea) 3 else 1
            )
        } else {
            Text(valor, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(start = 4.dp))
        }
    }
}
