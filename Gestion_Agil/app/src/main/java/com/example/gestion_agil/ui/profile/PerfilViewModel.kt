package com.example.gestion_agil.ui.profile

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.repository.UsuariosRepository
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuariosRepository

    private val _usuarioActual = MutableLiveData<Usuarios?>()
    val usuarioActual: LiveData<Usuarios?> get() = _usuarioActual

    //LiveData con resultado del logout (true/false + mensaje)
    private val _logoutResult = MutableLiveData<Pair<Boolean, String>>()
    val logoutResult: LiveData<Pair<Boolean, String>> get() = _logoutResult

    init {
        val dao = AppDatabase.getDatabase(application).UsuariosDao()
        repository = UsuariosRepository(dao)
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val prefs = getApplication<Application>().getSharedPreferences("user_session", 0)
        val idUsuario = prefs.getInt("id_usuario", -1)

        if (idUsuario != -1) {
            viewModelScope.launch {
                val usuario = repository.obtenerPorId(idUsuario)
                usuario?.let {
                    _usuarioActual.postValue(it)
                }
            }
        }
    }

    // Cerrar sesión (limpiar SharedPreferences)
    @SuppressLint("UseKtx")
    fun logout(nombreUsuario: String) {
        val prefs = getApplication<Application>().getSharedPreferences("user_session", 0)
        prefs.edit().clear().apply()
        _usuarioActual.postValue(null)

        //Emite el resultado usando Pair, igual que tu ejemplo de _authResult
        _logoutResult.postValue(Pair(true, "Has cerrado sesión, $nombreUsuario"))
    }
}