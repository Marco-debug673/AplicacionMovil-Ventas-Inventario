package com.example.gestion_agil.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.model.UsuariosDao

class UsuariosRepository(private val usuariosDao: UsuariosDao) {

    // Listado de todos los usuarios (útil para paneles de administración)
    val allUsuarios: LiveData<List<Usuarios>> = usuariosDao.getAllUsuarios()

    // --- OPERACIONES DE ESCRITURA ---

    suspend fun insert(usuarios: Usuarios): Boolean {
        return try {
            usuariosDao.insertUsuarios(usuarios)
            true // Inserción exitosa
        } catch (e: SQLiteConstraintException) {
            false // Error de restricción (ej. correo duplicado si es UNIQUE)
        }
    }

    suspend fun update(usuarios: Usuarios) {
        usuariosDao.updateUsuarios(usuarios)
    }

    suspend fun delete(usuarios: Usuarios) {
        usuariosDao.deleteUsuarios(usuarios)
    }

    // --- OPERACIONES DE BÚSQUEDA ---

    /**
     * Esta función es ahora la base de tu Login.
     * El ViewModel la llamará para obtener el usuario y luego comparará los hashes.
     */
    suspend fun obtenerPorCorreo(correo: String): Usuarios? {
        return usuariosDao.obtenerPorCorreo(correo)
    }

    suspend fun obtenerPorId(id: Int): Usuarios? {
        return usuariosDao.obtenerPorId(id)
    }
}