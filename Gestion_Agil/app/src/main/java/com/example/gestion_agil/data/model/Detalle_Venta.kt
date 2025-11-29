package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import androidx.room.ForeignKey

@Entity(
    tableName = "Detalle_Venta",
    indices = [Index(value = ["id_producto"]), Index(value = ["id_venta"])],
    foreignKeys = [
        ForeignKey(
            entity = Productos::class,
            parentColumns = ["id_producto"],
            childColumns = ["id_producto"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ventas::class,
            parentColumns = ["id_venta"],
            childColumns = ["id_venta"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class Detalle_Venta (
    @PrimaryKey(autoGenerate = true)
    val id_detalle_venta: Int = 0,

    val id_producto: Int,
    val id_venta: Int,

    val cantidad: Int,
    val subtotal: Double
)