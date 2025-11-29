package com.example.gestion_agil.ui.reflow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.repository.ProductosRepository

class HistorialInventarioViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: ProductosRepository
    val allProducts: LiveData<List<Productos>>

    init {
        val dao = AppDatabase.getDatabase(application).productDao()
        repository = ProductosRepository(dao)
        allProducts = repository.allProducts
    }
}