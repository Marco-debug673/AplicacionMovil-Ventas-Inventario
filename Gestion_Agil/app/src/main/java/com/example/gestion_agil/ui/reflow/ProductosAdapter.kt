package com.example.gestion_agil.ui.reflow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_agil.data.model.Productos
import com.example.gestion_agil.databinding.ItemProductoBinding
import com.example.gestion_agil.viewmodel.ProductosViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ProductosAdapter(
    private var productos: List<Productos>,
    private val viewModel: ProductosViewModel
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        with(holder.binding) {
            tvClaveProducto.text = "Clave: ${producto.clave_producto}"
            tvNombreProducto.text = "Nombre: ${producto.nombre_producto}"
            tvDescripcion.text = "Descripción: ${producto.descripcion}"
            tvPrecio.text = "Precio: $${producto.precio}"
            tvStock.text = "Stock: ${producto.stock_minimo}-${producto.stock_maximo}"
            tvFechaCaducidad.text = formatearFecha(producto.fecha_caducidad)
            tvEstado.text = if (producto.activo) "Activo" else "Inactivo"

            btnEditar.setOnClickListener {
                viewModel.selectProductoParaEditar(producto)
            }

            btnEliminar.setOnClickListener {
                viewModel.selectProductoParaEliminar(producto)
            }
        }
    }

    private fun formatearFecha(fechaOriginal: String): String {
        if (fechaOriginal.isNullOrEmpty()) return "Sin fecha"

        return try {
            val inputFormat = SimpleDateFormat("d MMM yyyy", Locale("es"))
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("es"))
            val date = inputFormat.parse(fechaOriginal)
            outputFormat.format(date ?: return "Sin fecha")
        } catch (e: Exception) {
            fechaOriginal
        }
    }

    override fun getItemCount(): Int = productos.size

    fun updateData(newList: List<Productos>) {
        productos = newList
        notifyDataSetChanged()
    }
}