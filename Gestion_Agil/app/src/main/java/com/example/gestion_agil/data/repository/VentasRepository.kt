package com.example.gestion_agil.data.repository

import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.data.model.VentasDao

class VentasRepository (private val ventasDao: VentasDao) {

    val allVentas: LiveData<List<Ventas>> = ventasDao.getAllVentas()

    suspend fun insert(ventas: Ventas) {
        ventasDao.insertVentas(ventas)
    }

    suspend fun update(ventas: Ventas) {
        ventasDao.updateVentas(ventas)
    }

    suspend fun delete(ventas: Ventas) {
        ventasDao.deleteVentas(ventas)
    }

    fun searchVentasByFecha(fecha: String): LiveData<List<Ventas>> {
        return ventasDao.searchVentasByFecha(fecha)
    }
}