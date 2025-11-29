package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notificaciones")
data class Notificacion (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_producto: Int?,
    val titulo: String,
    val mensaje: String,
    val fecha: String
)