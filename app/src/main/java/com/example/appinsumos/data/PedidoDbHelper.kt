package com.example.appinsumos.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PedidoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "insumos.db"
        private const val DATABASE_VERSION = 2 // Incremented version

        // Tabla Pedidos
        internal const val TABLE_PEDIDOS = "Pedidos"
        internal const val COLUMN_ID = "id"
        internal const val COLUMN_INSUMO = "insumo"
        internal const val COLUMN_CANTIDAD = "cantidad"
        internal const val COLUMN_ESTADO = "estado"
        internal const val COLUMN_FECHA = "fecha"
        internal const val COLUMN_PRIORIDAD = "prioridad"

        // Tabla Usuario
        internal const val TABLE_USUARIO = "Usuario"
        internal const val COLUMN_USER_ID = "id"
        internal const val COLUMN_NOMBRE = "nombre"
        internal const val COLUMN_RUT = "rut"
        internal const val COLUMN_EMAIL = "email"
        internal const val COLUMN_TELEFONO = "telefono"
        internal const val COLUMN_DIRECCION = "direccion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Pedidos
        val createPedidosTable = """
            CREATE TABLE $TABLE_PEDIDOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INSUMO TEXT NOT NULL,
                $COLUMN_CANTIDAD TEXT NOT NULL,
                $COLUMN_ESTADO TEXT NOT NULL,
                $COLUMN_FECHA TEXT NOT NULL,
                $COLUMN_PRIORIDAD TEXT NOT NULL
            )
        """.trimIndent()

        // Crear tabla Usuario
        val createUsuarioTable = """
            CREATE TABLE $TABLE_USUARIO (
                $COLUMN_USER_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_RUT TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_TELEFONO TEXT NOT NULL,
                $COLUMN_DIRECCION TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createPedidosTable)
        db.execSQL(createUsuarioTable)

        // Insertar usuario por defecto
        val insertUsuario = """
            INSERT INTO $TABLE_USUARIO VALUES (
                '1',
                'Juan Pérez González',
                '12.345.678-9',
                'juan.perez@email.com',
                '+56 9 8765 4321',
                'Av. Libertador Bernardo O''Higgins 1234, Santiago'
            )
        """.trimIndent()
        db.execSQL(insertUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEDIDOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }
}