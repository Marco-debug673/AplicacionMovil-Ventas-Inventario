package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VentasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVentas(ventas: Ventas)

    @Update
    suspend fun updateVentas(ventas: Ventas)

    @Delete
    suspend fun deleteVentas(ventas: Ventas)

    @Query("SELECT * FROM ventas ORDER BY fecha_venta DESC")
    fun getAllVentas(): LiveData<List<Ventas>>

    @Query("SELECT * FROM Ventas WHERE fecha_venta LIKE '%' || :fecha || '%' ORDER BY fecha_venta DESC")
    fun searchVentasByFecha(fecha: String): LiveData<List<Ventas>>
}