package com.example.gestion_agil.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gestion_agil.R
import com.example.gestion_agil.databinding.ActivityMainBinding
import com.example.gestion_agil.ui.profile.PerfilViewModel
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var perfilViewModel: PerfilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        perfilViewModel = ViewModelProvider(this)[PerfilViewModel::class.java]

        pedirPermisoNotificaciones()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_transform, R.id.nav_reflow, R.id.nav_profile
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView?.let { navView ->
            navView.setupWithNavController(navController)
            configurarHeaderNavigation(navView)
        }
        
        binding.appBarMain.contentMain?.root?.findViewById<NavigationView>(R.id.nav_view)?.let { navView ->
            navView.setupWithNavController(navController)
            configurarHeaderNavigation(navView)
        }

        binding.appBarMain.contentMain?.bottomNavView?.setupWithNavController(navController)

        programarCheckVencimientos()
        ejecutarWorkerInmediato()
    }

    private fun configurarHeaderNavigation(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.nav_header_name)
        val emailTextView = headerView.findViewById<TextView>(R.id.nav_header_email)

        perfilViewModel.usuarioActual.observe(this) { usuario ->
            if (usuario != null) {
                nameTextView.text = usuario.nombre_usuario
                emailTextView.text = ocultarCorreo(usuario.correo_electronico)
            }
        }
    }

    private fun ocultarCorreo(correo: String): String {
        val partes = correo.split("@")
        if (partes.size != 2) return correo

        val nombre = partes[0]
        val dominio = partes[1]

        return if (nombre.length > 1) {
            nombre[0] + "*".repeat(nombre.length - 1) + "@$dominio"
        } else {
            "*@$dominio"
        }
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Permiso de notificaciones ACEPTADO")
            } else {
                println("Permiso de notificaciones DENEGADO")
            }
        }
    }

    private fun programarCheckVencimientos() {
        val workRequest = PeriodicWorkRequestBuilder<CheckExpirationWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "check_vencimientos",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun ejecutarWorkerInmediato() {
        val testWork = OneTimeWorkRequestBuilder<CheckExpirationWorker>().build()
        WorkManager.getInstance(this).enqueue(testWork)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}