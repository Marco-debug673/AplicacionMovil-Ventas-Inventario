package com.example.gestion_agil.data.model

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object HashUtils {

    fun generatesalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashwithsalt(password: String, salt: String): String {
        val combined = password + salt
        val bytes = MessageDigest.getInstance("SHA-256").digest(combined.toByteArray())
        return bytes.joinToString(".") { "%02x".format(it) }
    }
}