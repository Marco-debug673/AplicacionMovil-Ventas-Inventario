package com.example.gestion_agil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.data.repository.VentasRepository
import kotlinx.coroutines.launch

class VentasViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: VentasRepository
    val allVentas: LiveData<List<Ventas>>

    // Nuevo LiveData para resultado de inserción
    private val _insertResult = MutableLiveData<Pair<Boolean, String>>()
    val insertResult: LiveData<Pair<Boolean, String>> get() = _insertResult

    private val _selectedVentaParaEliminar = MutableLiveData<Ventas?>()
    val selectedVentaParaEliminar: LiveData<Ventas?> get() = _selectedVentaParaEliminar

    init {
        val VentasDao = AppDatabase.getDatabase(application).ventasDao()
        repository = VentasRepository(VentasDao)
        allVentas = repository.allVentas
    }

    fun insert(Ventas: Ventas) = viewModelScope.launch{
        try {
            repository.insert(Ventas)
            _insertResult.postValue(Pair(true, "La venta se ha registrado correctamente."))
        } catch (e: Exception) {
            _insertResult.postValue(Pair(false, "Error al guardar la venta: ${e.message}"))
        }
    }

    fun update(Ventas: Ventas) = viewModelScope.launch{
        repository.update(Ventas)
    }

    fun delete(Ventas: Ventas) = viewModelScope.launch{
        repository.delete(Ventas)
    }

    fun selectVentaParaEliminar(venta: Ventas?) {
        _selectedVentaParaEliminar.postValue(venta)
    }
}