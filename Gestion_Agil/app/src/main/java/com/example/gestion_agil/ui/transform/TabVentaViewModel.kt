package com.example.gestion_agil.ui.transform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TabVentaViewModel : ViewModel() {

    // LiveData para el total que se mostrará en la UI
    private val _total = MutableLiveData<Double>()
    val total: LiveData<Double> get() = _total

    /**
     * Se llama cuando el texto del precio del producto cambia.
     * Convierte el texto a un número y actualiza el total.
     */
    fun onPrecioChanged(nuevoPrecio: String) {
        val precio = nuevoPrecio.toDoubleOrNull() ?: 0.0
        // Por ahora, el total es solo el precio. Más adelante se puede añadir cantidad, etc.
        _total.value = precio
    }
}
