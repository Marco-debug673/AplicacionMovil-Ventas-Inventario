package com.example.gestion_agil.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.gestion_agil.data.model.Sesion

class SesionRepository(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "sesion_segura",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun guardarSesion(idUsuario: Int) {
        sharedPreferences.edit().putInt("id_usuario", idUsuario).apply()
    }

    fun obtenerSesion(): Sesion? {
        val id = sharedPreferences.getInt("id_usuario", -1)
        return if (id != -1) Sesion(id) else null
    }

    fun cerrarSesion() {
        sharedPreferences.edit().remove("id_usuario").apply()
    }
}