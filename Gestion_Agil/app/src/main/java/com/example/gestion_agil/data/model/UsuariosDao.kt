package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsuariosDao {

    // --- OPERACIONES DE ESCRITURA (CUD) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarios(usuarios: Usuarios)

    @Update
    suspend fun updateUsuarios(usuarios: Usuarios)

    @Delete
    suspend fun deleteUsuarios(usuarios: Usuarios)

    // --- CONSULTAS DE BÚSQUEDA (READ) ---

    /**
     * Este método es el que usas en tu AuthViewModel para el Login.
     * Buscamos solo por correo para luego validar el Hash en la capa de lógica.
     */
    @Query("SELECT * FROM Usuarios WHERE correo_electronico = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuarios?

    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuarios?

    // --- CONSULTAS DE LISTADO Y RELACIONES ---

    @Query("SELECT * FROM Usuarios")
    fun getAllUsuarios(): LiveData<List<Usuarios>>

    @Transaction
    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id_usuario")
    fun obtenerUsuarioConProductos(id_usuario: Int): LiveData<UsuarioProductos>
}