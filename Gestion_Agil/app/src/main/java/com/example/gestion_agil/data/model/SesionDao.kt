package com.example.gestion_agil.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SesionDao {

    @Query("SELECT * FROM sesion LIMIT 1")
    suspend fun getSesion(): Sesion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSesion(sesion: Sesion)

    @Query("DELETE FROM sesion")
    suspend fun cerrarSesion()
}