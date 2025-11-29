package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsuariosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarios(usuarios: Usuarios)

    @Query("SELECT * FROM Usuarios")
    fun obtenerUsuarios(): LiveData<List<Usuarios>>

    @Transaction
    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id_usuario")
    fun obtenerUsuarioConProductos(id_usuario: Int): LiveData<UsuarioProductos>

    @Query("SELECT * FROM Usuarios WHERE correo_electronico = :correo AND clave_usuario = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuarios?

    @Query("SELECT * FROM Usuarios WHERE correo_electronico = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuarios?

    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuarios?

    @Update
    suspend fun updateUsuarios(usuarios: Usuarios)

    @Delete
    suspend fun deleteUsuarios(usuarios: Usuarios)

    @Query("SELECT * FROM Usuarios")
    fun getAllUsuarios(): LiveData<List<Usuarios>>
}