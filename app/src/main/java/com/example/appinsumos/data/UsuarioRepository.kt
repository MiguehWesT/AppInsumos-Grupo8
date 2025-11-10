package com.example.appinsumos.data

import android.content.ContentValues
import android.content.Context
import com.example.appinsumos.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository(context: Context) {
    private val dbHelper = PedidoDbHelper(context)

    suspend fun obtenerUsuario(): Usuario = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "Usuario",
            arrayOf("id", "nombre", "rut", "email", "telefono", "direccion"),
            "id = ?",
            arrayOf("1"),
            null, null, null
        )

        var usuario = Usuario() // Usuario por defecto
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                rut = cursor.getString(cursor.getColumnIndexOrThrow("rut")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))
            )
        }
        cursor.close()
        db.close()
        usuario
    }

    suspend fun actualizarUsuario(usuario: Usuario): Boolean = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("nombre", usuario.nombre)
                put("rut", usuario.rut)
                put("email", usuario.email)
                put("telefono", usuario.telefono)
                put("direccion", usuario.direccion)
            }
            val resultado = db.update("Usuario", values, "id = ?", arrayOf(usuario.id))
            db.close()
            resultado > 0
        } catch (e: Exception) {
            false
        }
    }
}