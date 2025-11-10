package com.example.appinsumos.model

import androidx.compose.ui.graphics.Color

enum class EstadoPedido(val texto: String, val emoji: String, val color: Color) {
    PENDIENTE("Pendiente", "⏳", Color(0xFFFFA726)),
    EN_PREPARACION("En preparación", "🏭", Color(0xFF42A5F5)),
    EN_REPARTO("En reparto", "🚚", Color(0xFF9C27B0)),
    ENTREGADO("Entregado", "✅", Color(0xFF66BB6A))
}