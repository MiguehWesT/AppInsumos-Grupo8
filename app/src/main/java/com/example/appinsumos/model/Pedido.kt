package com.example.appinsumos.model

data class Pedido(
    val id: String,
    val insumo: String,
    val cantidad: String,
    val estado: EstadoPedido,
    val fecha: String,
    val prioridad: String
)