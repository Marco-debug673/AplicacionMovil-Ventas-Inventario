package com.example.gestion_agil.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.model.UsuariosDao

class UsuariosRepository (private val usuariosDao: UsuariosDao) {

    val allUsuarios: LiveData<List<Usuarios>> = usuariosDao.getAllUsuarios()

    suspend fun insert(usuarios: Usuarios): Boolean {
        return try {
            usuariosDao.insertUsuarios(usuarios)
            true //insert éxitoso
        } catch (e: SQLiteConstraintException) {
            false //correo duplicado
        }
    }

    suspend fun update(usuarios: Usuarios) {
        usuariosDao.updateUsuarios(usuarios)
    }

    suspend fun delete(usuarios: Usuarios) {
        usuariosDao.deleteUsuarios(usuarios)
    }

    suspend fun login(correo: String, clave: String): Usuarios? {
        return usuariosDao.login(correo, clave)
    }

    suspend fun obtenerPorCorreo(correo: String): Usuarios? {
        return usuariosDao.obtenerPorCorreo(correo)
    }

    suspend fun obtenerPorId(id: Int): Usuarios? {
        return usuariosDao.obtenerPorId(id)
    }
}