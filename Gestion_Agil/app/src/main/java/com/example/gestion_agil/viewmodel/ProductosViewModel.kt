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

    // LiveData para mensajes de validación o errores (anulable para evitar repetición)
    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> get() = _statusMessage

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
        repository.insert(product)
    }

    fun update(product: Productos) = viewModelScope.launch {
        repository.update(product)
    }

    fun delete(product: Productos) = viewModelScope.launch {
        repository.delete(product)
    }

    fun showErrorMessage(message: String) {
        _statusMessage.postValue(message)
    }

    fun resetStatusMessage() {
        _statusMessage.value = null
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

    fun buscarPorNombre(nombre: String) {
        viewModelScope.launch {
            val producto = repository.getProductoByNombre(nombre)
            _productoEncontrado.postValue(producto)
        }
    }
}
