package com.example.gestion_agil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.model.UsuariosDao

class RecoveryPasswordViewModel(private val usuariosDao: UsuariosDao) : ViewModel() {

    suspend fun buscarUsuarioPorCorreo(correo: String): Usuarios? {
        return usuariosDao.obtenerPorCorreo(correo)
    }

    suspend fun guardarPin(usuario: Usuarios, pin: String) {
        val actualizado = usuario.copy(pin_recuperacion = pin)
        usuariosDao.updateUsuarios(actualizado)
    }

    suspend fun validarPin(correo: String, pin: String): Usuarios? {
        return usuariosDao.validarPin(correo, pin)
    }

    suspend fun buscarUsuarioPorId(id: Int): Usuarios? {
        return usuariosDao.obtenerPorId(id)
    }

    suspend fun actualizarClave(usuario: Usuarios, nuevaClave: String, nuevoSalt: String) {
        val actualizado = usuario.copy(
            clave_usuario = nuevaClave,
            salt = nuevoSalt,
            pin_recuperacion = null
        )
        usuariosDao.updateUsuarios(actualizado)
    }
}