package com.example.gestion_agil.data.repository

import com.example.gestion_agil.data.model.Sesion
import com.example.gestion_agil.data.model.SesionDao

class SesionRepository (private val sesionDao: SesionDao) {

    suspend fun guardarSesion(id_Usuario: Int) {
        sesionDao.guardarSesion(Sesion(id_Usuario))
    }

    suspend fun obtenerSesion(): Sesion? {
        return sesionDao.getSesion()
    }

    suspend fun cerrarSesion() {
        sesionDao.cerrarSesion()
    }
}