package com.example.gestion_agil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestion_agil.data.model.AppDatabase
import com.example.gestion_agil.data.model.HashUtils
import com.example.gestion_agil.data.model.Usuarios
import com.example.gestion_agil.data.repository.UsuariosRepository
import kotlinx.coroutines.launch

class AuthViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: UsuariosRepository

    init {
        val UsuariosDao =
            AppDatabase.getDatabase(application).UsuariosDao()
        repository = UsuariosRepository(UsuariosDao)
    }

    private val _authResult = MutableLiveData<Pair<Boolean, String>>()
    val authResult: LiveData<Pair<Boolean, String>> get() = _authResult

    // ================= REGISTRO =================
    fun register(nombre: String, correo: String, clave: String) {
        viewModelScope.launch {
            if (!isEmailValidate(correo)) {
                _authResult.postValue(Pair(false, "Ingrese un correo electrónico válido"))
                return@launch
            }

            if (!isPasswordValidate(clave)) {
                _authResult.postValue(
                    Pair(
                        false,
                        "La contraseña debe tener al menos 10 caracteres, una letra mayúscula, una minúscula, un número y un carácter especial"
                    )
                )
                return@launch
            }

            val usuarioExistente = repository.obtenerPorCorreo(correo)

            if (usuarioExistente != null) {
                _authResult.postValue(Pair(false, "El correo ya está registrado"))
                return@launch
            }

            val salt = HashUtils.generatesalt()
            val hashedPassword = HashUtils.hashwithsalt(clave, salt)

            val usuario = Usuarios(
                nombre_usuario = nombre,
                correo_electronico = correo,
                clave_usuario = hashedPassword,
                salt = salt
            )

            try {
                repository.insert(usuario)
                _authResult.postValue(Pair(true, "Usuario registrado correctamente"))
            } catch (e: Exception) {
                _authResult.postValue(Pair(false, "Error al registrar el usuario"))
            }
        }
    }

    fun isPasswordValidate(clave: String): Boolean {
        val regex = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*#'¡¿°|/(),.;:_?&-])[A-Za-z\\d@\$!%*#'¡¿°|/(),.;:_?&-]{10,}$")
        return regex.matches(clave)
    }

    fun isEmailValidate(correo: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@(gmail|hotmail|outlook|yahoo)\\.[A-Za-z]{2,6}$")
        return regex.matches(correo.trim())
    }

    // ================= LOGIN =================
    fun login(correo: String, clave: String) {
        viewModelScope.launch {
            if (!isEmailValidate(correo)) {
                _authResult.postValue(Pair(false, "Ingrese un correo electrónico válido"))
                return@launch
            }

            val usuario = repository.obtenerPorCorreo(correo)
            if (usuario == null) {
                _authResult.postValue(Pair(false, "El usuario no existe"))
                return@launch
            }

            val hashInput = HashUtils.hashwithsalt(clave, usuario.salt)
            if (hashInput == usuario.clave_usuario) {
                //Guardar sesión
                saveLoggedUser(
                    usuario.id_usuario,
                    usuario.nombre_usuario,
                    usuario.correo_electronico
                )
                //Mensaje con el nombre del usuario
                _authResult.postValue(Pair(true, "Bienvenido, ${usuario.nombre_usuario}"))
            } else {
                _authResult.postValue(Pair(false, "Contraseña incorrecta"))
            }
        }
    }

    fun saveLoggedUser(id: Int, nombre: String, correo: String) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("user_session", 0)
        with(sharedPrefs.edit()) {
            putInt("id_usuario", id)
            putString("nombre_usuario", nombre)
            putString("correo_electronico", correo)
            apply()
        }
    }

    fun insert(Usuarios: Usuarios) = viewModelScope.launch {
        repository.insert(Usuarios)
    }

    fun delete(Usuarios: Usuarios) = viewModelScope.launch {
        repository.delete(Usuarios)
    }

    fun update(Usuarios: Usuarios) = viewModelScope.launch {
        repository.update(Usuarios)
    }
    // ================= RECUPERAR CONTRASEÑA =================
    fun recoverPassword(email: String) {
        if (!isEmailValidate(email)) {
            _authResult.value = Pair(false, "Correo no válido")
            return
        }
    }
}