package com.example.gestion_agil.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gestion_agil.R
import com.example.gestion_agil.viewmodel.SplashViewModel

class LoginActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.cargando.value == true
        }

        splashViewModel.verificarSesion()

        splashViewModel.sesionActiva.observe(this) { idUsuario ->
            if (idUsuario != null) {
                iralHome()
            }
        }
    }

    private fun iralHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}