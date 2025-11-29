package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ventas")
data class Ventas (
    @PrimaryKey(autoGenerate = true)
    val id_venta:  Int = 0,
    val metodo_pago: String,
    val total_venta: Double,
    val cantidad_pago: Double,
    val cantidad_producto: Int,
    val fecha_venta: String,
    val cambio: Double
)