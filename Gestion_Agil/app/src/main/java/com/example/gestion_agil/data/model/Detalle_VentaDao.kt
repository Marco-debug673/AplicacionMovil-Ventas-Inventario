package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface Detalle_VentaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalle_Venta(detalle_Venta: Detalle_Venta)

    @Update
    suspend fun updateDetalle_Venta(detalle_Venta: Detalle_Venta)

    @Delete
    suspend fun deleteDetalle_Venta(detalle_Venta: Detalle_Venta)

    @Query("SELECT * FROM Detalle_Venta")
    fun getAllDetalle_Venta(): LiveData<List<Detalle_Venta>>
}