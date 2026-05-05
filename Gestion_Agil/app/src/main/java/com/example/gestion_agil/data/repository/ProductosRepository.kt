package com.example.gestion_agil.data.repository

import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.model.ProductosDao
import com.example.gestion_agil.utils.SecurityUtils

class ProductosRepository (private val productDao: ProductosDao) {

    val allProducts: LiveData<List<Productos>> = productDao.getAllProducts()

    fun getProductsByUser(userId: Int): LiveData<List<Productos>> {
        return productDao.getProductsByUser(userId)
    }

    suspend fun insert(product: Productos) {
        productDao.insertProductos(product)
    }

    suspend fun update(product: Productos) {
        productDao.updateProductos(product)
    }

    suspend fun delete(product: Productos) {
        productDao.deleteProductos(product)
    }

    // Búsqueda con desencriptación en memoria para permitir encontrar productos cifrados
    suspend fun getProductoByClave(clave: String): Productos? {
        val todos = productDao.getAllProductosList()
        return todos.find { SecurityUtils.decrypt(it.clave_producto) == clave }
    }

    suspend fun getProductoByNombre(nombre: String): Productos? {
        val todos = productDao.getAllProductosList()
        return todos.find { SecurityUtils.decrypt(it.nombre_producto).equals(nombre, ignoreCase = true) }
    }
}
