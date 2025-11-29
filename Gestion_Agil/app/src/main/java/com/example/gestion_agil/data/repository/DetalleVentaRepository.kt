package com.example.gestion_agil.data.repository

import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Detalle_Venta
import com.example.gestion_agil.data.model.Detalle_VentaDao

class DetalleVentaRepository (private val Detalle_VentaDao: Detalle_VentaDao) {

    val allDetalle_Venta: LiveData<List<Detalle_Venta>> = Detalle_VentaDao.getAllDetalle_Venta()

    suspend fun insert(detalleVenta: Detalle_Venta) {
        Detalle_VentaDao.insertDetalle_Venta(detalleVenta)
    }

    suspend fun update(detalleVenta: Detalle_Venta) {
        Detalle_VentaDao.updateDetalle_Venta(detalleVenta)
    }

    suspend fun delete(detalleVenta: Detalle_Venta) {
        Detalle_VentaDao.deleteDetalle_Venta(detalleVenta)
    }
}