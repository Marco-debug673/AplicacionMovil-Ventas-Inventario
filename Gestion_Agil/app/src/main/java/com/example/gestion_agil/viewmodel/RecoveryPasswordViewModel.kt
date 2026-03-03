package com.example.gestion_agil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.model.UsuariosDao

class RecoveryPasswordViewModel(private val usuariosDao: UsuariosDao) : ViewModel() {

    suspend fun buscarUsuarioPorCorreo(correo: String): Usuarios? {
        return usuariosDao.obtenerPorCorreo(correo)
    }

    suspend fun guardarPin(usuario: Usuarios, pin: String) {
        // Aquí podrías aplicar HashUtils al PIN si decides no guardarlo en texto plano
        val actualizado = usuario.copy(pin_recuperacion = pin)
        usuariosDao.updateUsuarios(actualizado)
    }

    /**
     * FUNCIÓN CORREGIDA:
     * Ahora busca al usuario por correo y compara el PIN en memoria.
     */
    suspend fun validarPin(correo: String, pinIngresado: String): Usuarios? {
        val usuario = usuariosDao.obtenerPorCorreo(correo)

        // Verificamos si el usuario existe y si el PIN coincide
        return if (usuario != null && usuario.pin_recuperacion == pinIngresado) {
            usuario
        } else {
            null
        }
    }

    suspend fun buscarUsuarioPorId(id: Int): Usuarios? {
        return usuariosDao.obtenerPorId(id)
    }

    suspend fun actualizarClave(usuario: Usuarios, nuevaClave: String, nuevoSalt: String) {
        val actualizado = usuario.copy(
            clave_usuario = nuevaClave,
            salt = nuevoSalt,
            pin_recuperacion = null // Limpiamos el PIN tras el cambio exitoso
        )
        usuariosDao.updateUsuarios(actualizado)
    }
}