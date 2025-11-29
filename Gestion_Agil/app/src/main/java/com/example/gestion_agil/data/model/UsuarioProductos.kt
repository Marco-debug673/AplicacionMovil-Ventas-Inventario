package com.example.gestion_agil.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioProductos (
    @Embedded val usuario: Usuarios,
    @Relation(
        parentColumn = "id_usuario",
        entityColumn = "id_usuario_foreign"
    )
    val productos: List<Productos>
)