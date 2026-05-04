package com.example.gestion_agil.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.Notificacion
import com.example.gestion_agil.data.repository.NotificacionRepository
import kotlinx.coroutines.launch

class SlideshowViewModel(
    val repository: NotificacionRepository
) : ViewModel() {

    val notificaciones: LiveData<List<Notificacion>> = repository.getNotificaciones()

    fun deleteNotificacion(notificacion: Notificacion) {
        viewModelScope.launch {
            repository.deleteNotificacion(notificacion)
        }
    }
}