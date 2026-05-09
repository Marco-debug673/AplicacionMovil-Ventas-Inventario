package com.example.gestion_agil.utils

import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecurityUtils {
    private const val KEY_ALIAS = "productos_encryption_key"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }

    private fun getSecretKey(): SecretKey {
        return try {
            val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
            existingKey?.secretKey ?: createKey()
        } catch (e: Exception) {
            createKey()
        }
    }

    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEY_STORE)
        keyGenerator.init(
            android.security.keystore.KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        return keyGenerator.generateKey()
    }

    fun encrypt(data: String?): String {
        if (data.isNullOrBlank()) return ""
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encryptedData = cipher.doFinal(data.trim().toByteArray(Charsets.UTF_8))
            val combined = iv + encryptedData
            // NO_WRAP evita saltos de línea que rompen la base de datos
            Base64.encodeToString(combined, Base64.NO_WRAP)
        } catch (e: Exception) {
            data ?: ""
        }
    }

    fun decrypt(encryptedDataWithIv: String?): String {
        if (encryptedDataWithIv.isNullOrBlank()) return ""
        return try {
            val cleaned = encryptedDataWithIv.trim()
            // DEFAULT es más tolerante al decodificar (acepta NO_WRAP y DEFAULT)
            val combined = Base64.decode(cleaned, Base64.DEFAULT)
            if (combined.size < 12) return cleaned
            
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val iv = combined.sliceArray(0 until 12)
            val encryptedData = combined.sliceArray(12 until combined.size)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            String(cipher.doFinal(encryptedData), Charsets.UTF_8).trim()
        } catch (e: Exception) {
            // Si falla, devolvemos el original limpio por si no estaba encriptado
            encryptedDataWithIv.trim()
        }
    }
}
