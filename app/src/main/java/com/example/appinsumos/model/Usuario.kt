package com.example.appinsumos.model

data class Usuario(
    val id: String = "1",
    val nombre: String = "Juan Pérez González",
    val rut: String = "12.345.678-9",
    val email: String = "juan.perez@email.com",
    val telefono: String = "+56 9 8765 4321",
    val direccion: String = "Av. Libertador Bernardo O'Higgins 1234, Santiago",
    val fotoUri: String? = null,      // 🆕 URI de la foto del perfil
    val ubicacion: String? = null     // 🆕 Última ubicación obtenida
)