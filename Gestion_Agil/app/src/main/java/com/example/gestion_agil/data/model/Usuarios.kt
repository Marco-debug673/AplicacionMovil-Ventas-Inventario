package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Usuarios",
    indices = [Index(value = ["correo_electronico"], unique = true)]
)
data class Usuarios (
    @PrimaryKey(autoGenerate = true)
    val id_usuario:  Int = 0,
    val nombre_usuario: String,
    val correo_electronico: String,
    val clave_usuario: String,
    val salt: String
)