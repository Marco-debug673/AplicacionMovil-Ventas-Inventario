package com.example.gestion_agil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel (application: Application) : AndroidViewModel(application) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<Pair<Boolean, String>>()
    val authResult: LiveData<Pair<Boolean, String>> get() = _authResult

    // ================= REGISTRO =================
    fun register(nombre: String, correo: String, clave: String) {

        if (!isEmailValidate(correo)) {
            _authResult.value = Pair(false, "Correo no válido")
            return
        }

        if (!isPasswordValidate(clave)) {
            _authResult.value = Pair(
                false,
                "La contraseña debe tener mínimo 10 caracteres, una mayúscula, una minúscula, un número y carácter especial"
            )
            return
        }

        mAuth.createUserWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = mAuth.currentUser

                    // Guardar nombre en perfil Firebase
                    val profileUpdates = userProfileChangeRequest {
                        displayName = nombre
                    }

                    user?.updateProfile(profileUpdates)

                    // Enviar verificación de correo
                    user?.sendEmailVerification()

                    _authResult.value =
                        Pair(true, "Registro éxitoso. Revisar tu correo para verificar tu cuenta")

                } else {
                    val error = task.exception?.message ?: "Error al registrar usuario"
                    when {
                        error.contains("already in use") ->
                            _authResult.value = Pair(false, "Este correo ya está registrado")

                        else ->
                            _authResult.value = Pair(false, "Error al crear cuenta")
                    }
                }
            }
    }

    // ================= LOGIN =================
    fun login(correo: String, clave: String) {

        if (correo.isEmpty() || clave.isEmpty()) {
            _authResult.value = Pair(false, "Completa todos los campos")
            return
        }

        mAuth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    if (user != null && user.isEmailVerified) {
                        _authResult.value = Pair(true, "Bienvenido ${user.displayName}")
                    } else {
                        mAuth.signOut()
                        _authResult.value = Pair(false, "Debes verificar tu correo antes de iniciar sesión")
                    }

                } else {
                    _authResult.value = Pair(false, "El correo o la contraseña no es correcto")
                }
            }
    }
    // ================= RECUPERAR CONTRASEÑA =================
    fun recoverPassword(email: String) {

        if (!isEmailValidate(email)) {
            _authResult.value = Pair(false, "Correo no válido")
            return
        }

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = Pair(true, "Correo de recuperación enviado")
                } else {
                    _authResult.value = Pair(false, "No se pudo enviar el correo")
                }
            }
    }
    // ================= VALIDACIONES =================
    private fun isPasswordValidate(password: String): Boolean {
        return password.length >= 10 &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() }
    }

    private fun isEmailValidate(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}