package com.example.appinsumos.model

data class Insumo(
    val id: String,
    val nombre: String,
    val descripcion: String = "",
    val disponible: Boolean = true
)