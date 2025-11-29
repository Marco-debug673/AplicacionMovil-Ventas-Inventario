package com.example.gestion_agil.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gestion_agil.data.model.Notificacion
import com.example.gestion_agil.data.repository.NotificacionRepository

class SlideshowViewModel(
    val repository: NotificacionRepository
) : ViewModel() {

    val notificaciones: LiveData<List<Notificacion>> = repository.getNotificaciones()
}