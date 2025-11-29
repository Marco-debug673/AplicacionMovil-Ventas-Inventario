package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificacionDao {
    @Insert
    suspend fun insertNotificacion(notificacion: Notificacion)

    @Query("SELECT * FROM notificaciones ORDER BY fecha DESC")
    fun getAllNotificaciones(): LiveData<List<Notificacion>>

    @Query("SELECT * FROM Notificaciones ORDER BY fecha DESC")
    suspend fun getAllNotificacionesList(): List<Notificacion>

    @Delete
    suspend fun deleteNotificacion(notificacion: Notificacion)
}