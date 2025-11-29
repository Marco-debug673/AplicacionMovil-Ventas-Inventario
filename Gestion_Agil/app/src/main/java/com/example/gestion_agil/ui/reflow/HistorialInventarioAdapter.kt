package com.example.gestion_agil.ui.reflow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.databinding.ItemProductoBinding

class HistorialInventarioAdapter (

    private var productosList: List<Productos>
) : RecyclerView.Adapter<HistorialInventarioAdapter.ProductoViewHolder>() {

    private var productosOriginal: List<Productos> = productosList

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Productos) {
            binding.tvClaveProducto.text = producto.clave_producto
            binding.tvNombreProducto.text = producto.nombre_producto
            binding.tvDescripcion.text = producto.descripcion
            binding.tvPrecio.text = "Precio: $${producto.precio}"
            binding.tvStock.text = "Stock: ${producto.stock_minimo} - ${producto.stock_maximo}"
            binding.tvEstado.text = if (producto.activo) "Activo" else "Inactivo"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductoViewHolder(binding)
    }

    override fun getItemCount(): Int = productosList.size

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productosList[position])
    }

    fun updateList(newList: List<Productos>) {
        productosList = newList
        productosOriginal = newList
        notifyDataSetChanged()
    }

    fun filterList(query: String) {
        productosList = if (query.isEmpty()) {
            productosOriginal
        } else {
            productosOriginal.filter {
                it.nombre_producto.contains(query, ignoreCase = true) ||
                        it.clave_producto.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}