package com.example.gestion_agil.ui.transform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.data.repository.VentasRepository

class TabHistorialViewModelFactory(private val repository: VentasRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabHistorialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TabHistorialViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}