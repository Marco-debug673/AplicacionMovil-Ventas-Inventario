package com.example.gestion_agil.ui.transform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.data.repository.VentasRepository

class TabHistorialViewModel(
    private val repository: VentasRepository
) : ViewModel() {

    private val searchQuery = MutableLiveData<String?>("")

    val historial: LiveData<List<Ventas>> = searchQuery.switchMap { query: String? ->
        if (query.isNullOrEmpty()) repository.allVentas
        else repository.searchVentasByFecha(query)
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery.value = query
    }
}