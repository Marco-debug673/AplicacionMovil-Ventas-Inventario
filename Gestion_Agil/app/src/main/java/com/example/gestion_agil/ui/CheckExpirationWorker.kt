package com.example.gestion_agil.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gestion_agil.data.model.AppDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class CheckExpirationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val productosDao = AppDatabase.getDatabase(context).productDao()
        val productos = productosDao.getAllProductosList()

        val hoy = LocalDate.now()

        // FORMATO DE FECHA: "17 nov 2025"
        @Suppress("DEPRECATION") val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es"))

        productos.forEach { producto ->

            val fechaCad = LocalDate.parse(producto.fecha_caducidad.lowercase(), formatter)
            val diasRestantes = ChronoUnit.DAYS.between(hoy, fechaCad)

            // NOTIFICACIÓN DE 3 DÍAS
            if (diasRestantes == 3L && !producto.notificado_3dias) {

                mostrarNotificacion(
                    "Producto cerca de vencer",
                    "El producto ${producto.nombre_producto} vence en 3 días"
                )

                producto.notificado_3dias = true
                productosDao.updateProductos(producto)
            }

            // NOTIFICACIÓN DEL DÍA DE CADUCIDAD
            if (diasRestantes == 0L && !producto.notificado_hoy) {

                mostrarNotificacion(
                    "Producto vencido",
                    "El producto ${producto.nombre_producto} vence hoy"
                )

                producto.notificado_hoy = true
                productosDao.updateProductos(producto)
            }
        }

        return Result.success()
    }

    private fun mostrarNotificacion(titulo: String, mensaje: String) {
        val channelId = "vencimiento_channel"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de vencimiento",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        manager.notify((0..9999).random(), notification)
    }
}