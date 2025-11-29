package com.example.gestion_agil.data.repository

import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.model.ProductosDao


class ProductosRepository (private val productDao: ProductosDao) {

    val allProducts: LiveData<List<Productos>> = productDao.getAllProducts()

    suspend fun insert(product: Productos) {
        productDao.insertProductos(product)
    }

    suspend fun update(product: Productos) {
        productDao.updateProductos(product)
    }

    suspend fun delete(product: Productos) {
        productDao.deleteProductos(product)
    }

    suspend fun getProductoByClave(clave: String): Productos? {
        return productDao.getProductoByClave(clave)
    }
}