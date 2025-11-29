package com.example.gestion_agil.ui.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_agil.data.model.Notificacion
import com.example.gestion_agil.databinding.ItemNotificationBinding

class NotificacionAdapter(
    private var lista: List<Notificacion>
) : RecyclerView.Adapter<NotificacionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = lista[position]

        holder.binding.apply {
            textTitulo.text = n.titulo
            textMensaje.text = n.mensaje
            textFecha.text = n.fecha
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateData(newList: List<Notificacion>) {
        lista = newList
        notifyDataSetChanged()
    }
}