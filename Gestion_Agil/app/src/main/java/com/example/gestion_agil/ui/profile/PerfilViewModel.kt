package com.example.gestion_agil.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class PerfilViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()

    private val _usuarioFirebase = MutableLiveData<FirebaseUser?>()
    val usuarioFirebase: LiveData<FirebaseUser?> get() = _usuarioFirebase

    private val _logoutResult = MutableLiveData<Pair<Boolean, String>>()
    val logoutResult: LiveData<Pair<Boolean, String>> get() = _logoutResult

    init {
        _usuarioFirebase.value = mAuth.currentUser
    }

    fun logout() {
        mAuth.signOut()
        _logoutResult.value = Pair(true, "Sesión cerrada correctamente")
    }

    fun isUserLogged(): Boolean {
        return mAuth.currentUser != null
    }
}