package com.example.gestion_agil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesion")
data class Sesion (
    @PrimaryKey val id_usuario: Int
)