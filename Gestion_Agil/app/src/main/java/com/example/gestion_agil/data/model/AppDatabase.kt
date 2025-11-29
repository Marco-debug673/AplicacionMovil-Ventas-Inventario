package com.example.gestion_agil.data.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Productos::class, Usuarios::class, Ventas::class, Detalle_Venta::class, Notificacion::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductosDao
    abstract fun UsuariosDao(): UsuariosDao
    abstract fun ventasDao(): VentasDao
    abstract fun Detalle_ventaDao(): Detalle_VentaDao
    abstract fun NotificacionDao(): NotificacionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Tienda_db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}