package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import androidx.room.ForeignKey

@Entity(
    tableName = "Productos",
    foreignKeys = [
        ForeignKey(
            entity = Usuarios::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario_foreign"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario_foreign"])],
)
data class Productos (
    @PrimaryKey(autoGenerate = true)
    val id_producto: Int = 0,
    val clave_producto: String,
    val nombre_producto: String,
    val descripcion: String,
    var precio: Double,
    val stock_minimo: Int,
    val stock_maximo: Int,
    val fecha_caducidad: String,
    var activo: Boolean,
    @ColumnInfo(name = "id_usuario_foreign")
    val idUsuarioForeign: Int,

    var notificado_3dias: Boolean = false,
    var notificado_hoy: Boolean = false
)