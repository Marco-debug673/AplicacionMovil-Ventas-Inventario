package com.example.gestion_agil.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.Notificacion
import com.example.gestion_agil.utils.SecurityUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.Locale

class CheckExpirationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // Formateador flexible para manejar variaciones de mayúsculas/minúsculas y puntos en el mes
    private val flexibleFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("d MMM yyyy")
        .toFormatter(Locale("es"))

    override suspend fun doWork(): Result {
        val productosDao = AppDatabase.getDatabase(context).productDao()
        val productos = productosDao.getAllProductosList()

        val hoy = LocalDate.now()

        productos.forEach { producto ->
            try {
                // Normalizar la fecha: quitar puntos y limpiar espacios
                val fechaLimpia = producto.fecha_caducidad.replace(".", "").trim()
                val fechaCad = LocalDate.parse(fechaLimpia, flexibleFormatter)
                
                val diasRestantes = ChronoUnit.DAYS.between(hoy, fechaCad)

                // Desencriptar el nombre del producto para que aparezca legible en la notificación
                val nombreDesencriptado = SecurityUtils.decrypt(producto.nombre_producto)

                // NOTIFICACIÓN DE 3 DÍAS
                if (diasRestantes == 3L && !producto.notificado_3dias) {
                    mostrarNotificacion(
                        "Producto cerca de vencer",
                        "El producto $nombreDesencriptado vence en 3 días",
                        producto.id_producto,
                        producto.idUsuarioForeign
                    )
                    producto.notificado_3dias = true
                    productosDao.updateProductos(producto)
                }

                // NOTIFICACIÓN DEL DÍA DE CADUCIDAD
                if (diasRestantes == 0L && !producto.notificado_hoy) {
                    mostrarNotificacion(
                        "Producto vencido",
                        "El producto $nombreDesencriptado vence hoy",
                        producto.id_producto,
                        producto.idUsuarioForeign
                    )
                    producto.notificado_hoy = true
                    productosDao.updateProductos(producto)
                }
            } catch (e: Exception) {
                // Si la fecha no se puede parsear, se ignora ese producto específico
            }
        }

        return Result.success()
    }

    private suspend fun mostrarNotificacion(titulo: String, mensaje: String, idProducto: Int? = null, idUsuario: Int) {
        val channelId = "vencimiento_channel"

        // Guardar en la base de datos (se guarda el mensaje ya desencriptado)
        val database = AppDatabase.getDatabase(context)
        val notificacionDao = database.NotificacionDao()
        val fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale("es")))

        val nuevaNotificacion = Notificacion(
            id_producto = idProducto,
            id_usuario = idUsuario,
            titulo = titulo,
            mensaje = mensaje,
            fecha = fechaActual
        )
        notificacionDao.insertNotificacion(nuevaNotificacion)

        // Mostrar notificación en el sistema
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify((0..9999).random(), notification)
    }
}
