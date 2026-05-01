package com.example.gestion_agil.data.model

import org.mindrot.jbcrypt.BCrypt

object HashUtils {

    // Hashear contraseña
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Verificar contraseña
    fun verifyPassword(password: String, hash: String): Boolean {
        return try {
            BCrypt.checkpw(password, hash)
        } catch (e: Exception) {
            false
        }
    }
}