package com.example.appinsumos.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.appinsumos.model.EstadoPedido
import com.example.appinsumos.model.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoRepository(context: Context) {
    private val dbHelper = PedidoDbHelper(context)

    suspend fun obtenerPedidos(): List<Pedido> = withContext(Dispatchers.IO) {
        val pedidos = mutableListOf<Pedido>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            PedidoDbHelper.TABLE_PEDIDOS,
            arrayOf(
                PedidoDbHelper.COLUMN_ID,
                PedidoDbHelper.COLUMN_INSUMO,
                PedidoDbHelper.COLUMN_CANTIDAD,
                PedidoDbHelper.COLUMN_ESTADO,
                PedidoDbHelper.COLUMN_FECHA,
                PedidoDbHelper.COLUMN_PRIORIDAD
            ),
            null, null, null, null, "${PedidoDbHelper.COLUMN_ID} DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_ID))
                val insumo = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_INSUMO))
                val cantidad = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_CANTIDAD))
                val estadoStr = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_ESTADO))
                val fecha = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_FECHA))
                val prioridad = getString(getColumnIndexOrThrow(PedidoDbHelper.COLUMN_PRIORIDAD))

                val estado = when (estadoStr) {
                    "PENDIENTE" -> EstadoPedido.PENDIENTE
                    "EN_PREPARACION" -> EstadoPedido.EN_PREPARACION
                    "EN_REPARTO" -> EstadoPedido.EN_REPARTO
                    "ENTREGADO" -> EstadoPedido.ENTREGADO
                    else -> EstadoPedido.PENDIENTE
                }

                pedidos.add(Pedido(id, insumo, cantidad, estado, fecha, prioridad))
            }
        }
        cursor.close()
        db.close()
        pedidos
    }

    suspend fun obtenerPedidoPorId(id: String): Pedido? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PedidoDbHelper.TABLE_PEDIDOS,
            arrayOf(
                PedidoDbHelper.COLUMN_ID,
                PedidoDbHelper.COLUMN_INSUMO,
                PedidoDbHelper.COLUMN_CANTIDAD,
                PedidoDbHelper.COLUMN_ESTADO,
                PedidoDbHelper.COLUMN_FECHA,
                PedidoDbHelper.COLUMN_PRIORIDAD
            ),
            "${PedidoDbHelper.COLUMN_ID} = ?",
            arrayOf(id),
            null, null, null
        )

        var pedido: Pedido? = null
        if (cursor.moveToFirst()) {
            val insumo = cursor.getString(cursor.getColumnIndexOrThrow(PedidoDbHelper.COLUMN_INSUMO))
            val cantidad = cursor.getString(cursor.getColumnIndexOrThrow(PedidoDbHelper.COLUMN_CANTIDAD))
            val estadoStr = cursor.getString(cursor.getColumnIndexOrThrow(PedidoDbHelper.COLUMN_ESTADO))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow(PedidoDbHelper.COLUMN_FECHA))
            val prioridad = cursor.getString(cursor.getColumnIndexOrThrow(PedidoDbHelper.COLUMN_PRIORIDAD))

            val estado = when (estadoStr) {
                "PENDIENTE" -> EstadoPedido.PENDIENTE
                "EN_PREPARACION" -> EstadoPedido.EN_PREPARACION
                "EN_REPARTO" -> EstadoPedido.EN_REPARTO
                "ENTREGADO" -> EstadoPedido.ENTREGADO
                else -> EstadoPedido.PENDIENTE
            }

            pedido = Pedido(id, insumo, cantidad, estado, fecha, prioridad)
        }
        cursor.close()
        db.close()
        pedido
    }

    suspend fun crearPedido(insumo: String, cantidad: String, prioridad: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val fechaActual = obtenerFechaActual()

            val values = ContentValues().apply {
                put(PedidoDbHelper.COLUMN_INSUMO, insumo)
                put(PedidoDbHelper.COLUMN_CANTIDAD, cantidad)
                put(PedidoDbHelper.COLUMN_ESTADO, "PENDIENTE")
                put(PedidoDbHelper.COLUMN_FECHA, fechaActual)
                put(PedidoDbHelper.COLUMN_PRIORIDAD, prioridad)
            }

            val resultado = db.insert(PedidoDbHelper.TABLE_PEDIDOS, null, values)
            db.close()
            resultado != -1L
        } catch (e: Exception) {
            // Log the exception for debugging
            // Log.e("PedidoRepository", "Error creating order", e)
            false
        }
    }

    suspend fun actualizarEstadoPedido(id: String, nuevoEstado: EstadoPedido): Boolean = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(PedidoDbHelper.COLUMN_ESTADO, nuevoEstado.name)
            }
            val resultado = db.update(PedidoDbHelper.TABLE_PEDIDOS, values, "${PedidoDbHelper.COLUMN_ID} = ?", arrayOf(id))
            db.close()
            resultado > 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun eliminarPedido(id: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val resultado = db.delete(PedidoDbHelper.TABLE_PEDIDOS, "${PedidoDbHelper.COLUMN_ID} = ?", arrayOf(id))
            db.close()
            resultado > 0
        } catch (e: Exception) {
            false
        }
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}