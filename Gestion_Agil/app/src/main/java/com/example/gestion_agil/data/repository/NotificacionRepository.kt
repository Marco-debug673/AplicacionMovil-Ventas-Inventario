package com.example.gestion_agil.data.repository

import androidx.lifecycle.LiveData
import com.example.gestion_agil.data.model.Notificacion
import com.example.gestion_agil.data.model.NotificacionDao

class NotificacionRepository(private val dao: NotificacionDao) {

    suspend fun insertNotificacion(notificacion: Notificacion) {
        dao.insertNotificacion(notificacion)
    }

    fun getNotificaciones(): LiveData<List<Notificacion>> {
        return dao.getAllNotificaciones()
    }
}