package com.example.gestion_agil.data.model

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object HashUtils {

    private val argon2: Argon2 = Argon2Factory.create()

    // Hashear contraseña
    fun hashPassword(password: String): String {
        return argon2.hash(
            3,          // Hashear contraseña
            65536,      // memoria (64 MB)
            1,        // paralelismo
            password.toCharArray()
        )
    }

    // Verificar contraseña
    fun verifyPassword(password: String, hash: String): Boolean {
        return argon2.verify(hash, password.toCharArray())
    }
}