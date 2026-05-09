package com.example.gestion_agil.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Productos::class,
    Usuarios::class,
    Ventas::class,
    Detalle_Venta::class,
    Notificacion::class,
    Sesion::class], version = 7, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductosDao
    abstract fun UsuariosDao(): UsuariosDao
    abstract fun ventasDao(): VentasDao
    abstract fun Detalle_VentaDao(): Detalle_VentaDao
    abstract fun NotificacionDao(): NotificacionDao
    abstract fun SesionDao(): SesionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migración de la versión 6 a la 7 para añadir el campo id_usuario en Notificaciones
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Intentamos añadir la columna por si no existe. 
                // Si el error persiste, es posible que otros campos hayan cambiado.
                database.execSQL("ALTER TABLE Notificaciones ADD COLUMN id_usuario INTEGER NOT NULL DEFAULT -1")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Tienda_db"
                )
                    .addMigrations(MIGRATION_6_7)
                    // .fallbackToDestructiveMigration() // Solo usar si no importa perder datos de prueba
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
