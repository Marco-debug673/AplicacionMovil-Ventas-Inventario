package com.example.gestion_agil.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.repository.SesionRepository
import com.example.gestion_agil.data.repository.UsuariosRepository
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val usuariosRepository: UsuariosRepository
    private val sesionRepository: SesionRepository

    private val _usuarioActual = MutableLiveData<Usuarios?>()
    val usuarioActual: LiveData<Usuarios?> get() = _usuarioActual

    //LiveData con resultado del logout (true/false + mensaje)
    private val _logoutResult = MutableLiveData<Pair<Boolean, String>>()
    val logoutResult: LiveData<Pair<Boolean, String>> get() = _logoutResult

    init {
        val db = AppDatabase.getDatabase(application)

        usuariosRepository = UsuariosRepository(db.UsuariosDao())
        sesionRepository = SesionRepository(db.SesionDao())

        cargarUsuarioActual()
    }

    private fun cargarUsuarioActual() {
        viewModelScope.launch {
            val sesion = sesionRepository.obtenerSesion()

            if (sesion != null) {
                val usuario = usuariosRepository.obtenerPorId(sesion.id_usuario)
                _usuarioActual.postValue(usuario)
            } else {
                _usuarioActual.postValue(null)
            }
        }
    }

    // Cerrar sesión
    fun logout(nombreUsuario: String) {
        viewModelScope.launch {
            sesionRepository.cerrarSesion() // Limpia tabla 'sesion'
            _usuarioActual.postValue(null)
            _logoutResult.postValue(Pair(true, "Has cerrado sesión, $nombreUsuario"))
        }
    }
}