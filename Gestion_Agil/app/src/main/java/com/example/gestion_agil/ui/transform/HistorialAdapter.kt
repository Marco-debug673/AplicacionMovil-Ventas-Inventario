package com.example.gestion_agil.ui.transform

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_agil.data.model.Ventas
import com.example.gestion_agil.databinding.ItemVentaBinding
import com.example.gestion_agil.viewmodel.VentasViewModel
import java.text.NumberFormat

class HistorialAdapter(private var ventasList: List<Ventas>, private val ventasViewModel: VentasViewModel) :
    RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(val binding: ItemVentaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val binding = ItemVentaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistorialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val venta = ventasList[position]
        val format = NumberFormat.getCurrencyInstance()

        with(holder.binding) {
            tvFechaVenta.text = "Fecha: ${venta.fecha_venta}"
            tvMetodoPago.text = "Método de pago: ${venta.metodo_pago}"
            tvCantidad.text = "Cantidad: ${venta.cantidad_producto} productos"
            tvTotal.text = "Total: ${format.format(venta.total_venta)}"
            tvCambio.text = "Cambio: ${format.format(venta.cambio)}"

            btnEliminarVenta.setOnClickListener {
                ventasViewModel.selectVentaParaEliminar(venta)
            }
        }
    }

    override fun getItemCount() = ventasList.size

    fun updateData(newList: List<Ventas>) {
        ventasList = newList
        notifyDataSetChanged()
    }
}