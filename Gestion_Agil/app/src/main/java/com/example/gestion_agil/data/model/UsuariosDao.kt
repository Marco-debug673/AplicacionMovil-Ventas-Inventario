package com.example.gestion_agil.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

/*
 * DAO (Data Access Object)
 * Esta interfaz define todas las operaciones que se pueden hacer
 * sobre la tabla "Usuarios" en la base de datos Room.
 */

@Dao
interface UsuariosDao {

    /*
     * Inserta un nuevo usuario en la base de datos.
     * Si el correo ya existe (índice único), lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarios(usuario: Usuarios)

    /*
     * Obtiene todos los usuarios registrados.
     * Devuelve LiveData para que la UI se actualice automáticamente.
     */
    @Query("SELECT * FROM Usuarios")
    fun obtenerUsuarios(): LiveData<List<Usuarios>>

    /*
     * Obtiene un usuario junto con sus productos asociados.
     * @Transaction asegura que la consulta sea atómica.
     */
    @Transaction
    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id_usuario")
    fun obtenerUsuarioConProductos(id_usuario: Int): LiveData<UsuarioProductos>

    /*
     * Busca un usuario por su correo electrónico.
     * Se usa en el proceso de login para luego validar la contraseña con hash.
     */
    @Query("SELECT * FROM Usuarios WHERE correo_electronico = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuarios?

    /*
     * Obtiene un usuario por su ID.
     * Útil cuando ya existe sesión activa.
     */
    @Query("SELECT * FROM Usuarios WHERE id_usuario = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuarios?

    /*
     * Actualiza los datos de un usuario existente.
     */
    @Update
    suspend fun updateUsuarios(usuario: Usuarios)

    /*
     * Elimina un usuario de la base de datos.
     */
    @Delete
    suspend fun deleteUsuarios(usuario: Usuarios)

    /*
     * Obtiene todos los usuarios (alternativa a obtenerUsuarios).
     */
    @Query("SELECT * FROM Usuarios")
    fun getAllUsuarios(): LiveData<List<Usuarios>>

    /*
     * Valida el PIN de recuperación de contraseña.
     * Se usa cuando el usuario intenta recuperar su cuenta.
     */
    @Query("SELECT * FROM Usuarios WHERE correo_electronico = :correo AND pin_recuperacion = :pin LIMIT 1")
    suspend fun validarPin(correo: String, pin: String): Usuarios?
}