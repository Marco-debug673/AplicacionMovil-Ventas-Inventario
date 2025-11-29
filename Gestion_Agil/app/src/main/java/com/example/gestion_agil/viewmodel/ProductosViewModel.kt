package com.example.gestion_agil.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.data.repository.ProductosRepository
import kotlinx.coroutines.launch

class ProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductosRepository
    val allProducts: LiveData<List<Productos>>
    private val _productoEncontrado = MutableLiveData<Productos?>()
    val productoEncontrado: LiveData<Productos?> get() = _productoEncontrado

    private val _insertResult = MutableLiveData<Pair<Boolean, String>>()
    val insertResult: LiveData<Pair<Boolean, String>> get() = _insertResult

    private val _selectedProductoParaEditar = MutableLiveData<Productos?>()
    val selectedProductoParaEditar: LiveData<Productos?> get() = _selectedProductoParaEditar

    private val _selectedProductoParaEliminar = MutableLiveData<Productos?>()
    val selectedProductoParaEliminar: LiveData<Productos?> get() = _selectedProductoParaEliminar

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductosRepository(productDao)
        allProducts = repository.allProducts
    }

    fun insert(product: Productos) = viewModelScope.launch {
        try {
            repository.insert(product)
            _insertResult.postValue(Pair(true, "Producto guardado correctamente"))
        } catch (e: Exception) {
            _insertResult.postValue(Pair(false, "Error al guardar el producto: ${e.message}"))
        }
    }

    fun update(product: Productos) = viewModelScope.launch {
        try {
            repository.update(product)
            _insertResult.postValue(Pair(true, "Producto actualizado correctamente"))
        } catch (e: Exception) {
            _insertResult.postValue(Pair(false, "Error al actualizar: ${e.message}"))
        }
    }

    fun delete(product: Productos) = viewModelScope.launch {
        try {
            repository.delete(product)
            _insertResult.postValue(Pair(true, "Producto eliminado correctamente"))
        } catch (e: Exception) {
            _insertResult.postValue(Pair(false, "Error al eliminar: ${e.message}"))
        }
    }

    fun showErrorMessage(message: String) {
        _insertResult.postValue(Pair(false, message))
    }

    fun selectProductoParaEditar(product: Productos?) {
        _selectedProductoParaEditar.postValue(product)
    }

    fun selectProductoParaEliminar(product: Productos?) {
        _selectedProductoParaEliminar.postValue(product)
    }

    fun buscarPorClave(clave: String) {
        viewModelScope.launch {
            val producto = repository.getProductoByClave(clave)
            _productoEncontrado.postValue(producto)
        }
    }
}