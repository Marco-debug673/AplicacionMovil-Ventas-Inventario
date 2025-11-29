package com.example.gestion_agil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.repository.UsuariosRepository
import kotlinx.coroutines.launch

class UsuariosViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: UsuariosRepository
    val allUsuarios: LiveData<List<Usuarios>>

    init {
        val UsuariosDao = AppDatabase.getDatabase(application).UsuariosDao()
        repository = UsuariosRepository(UsuariosDao)
        allUsuarios = repository.allUsuarios
    }

    fun insert(Usuarios: Usuarios) = viewModelScope.launch {
        repository.insert(Usuarios)
    }

    fun delete(Usuarios: Usuarios) = viewModelScope.launch {
        repository.delete(Usuarios)
    }

    fun update(Usuarios: Usuarios) = viewModelScope.launch {
        repository.update(Usuarios)
    }
}