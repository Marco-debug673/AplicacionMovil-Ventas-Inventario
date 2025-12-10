package com.example.gestion_agil.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.data.model.AppDatabase

class RecoveryPasswordViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecoveryPasswordViewModel::class.java)) {
            val dao = AppDatabase.getDatabase(context).UsuariosDao()
            return RecoveryPasswordViewModel(dao) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}