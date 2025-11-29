package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductos(productos: Productos)

    @Update
    suspend fun updateProductos(productos: Productos)

    @Delete
    suspend fun deleteProductos(productos: Productos)

    @Query("SELECT * FROM productos ORDER BY nombre_producto ASC")
    fun getAllProducts(): LiveData<List<Productos>>

    @Query("SELECT * FROM Productos WHERE clave_producto = :clave LIMIT 1")
    suspend fun getProductoByClave(clave: String): Productos?

    @Query("SELECT * FROM productos")
    suspend fun getAllProductosList(): List<Productos>
}