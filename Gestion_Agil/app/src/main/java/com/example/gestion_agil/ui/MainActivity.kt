package com.example.gestion_agil.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        pedirPermisoNotificaciones()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_profile
            ), binding.drawerLayout
        )

        // Configura el ActionBar (la barra superior) para que muestre el título del fragmento
        // y el botón de navegación del Drawer (menú lateral).
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Conecta el DrawerLayout (menú lateral) con el NavController.
        binding.navView?.setupWithNavController(navController)
        binding.appBarMain.contentMain?.bottomNavView?.setupWithNavController(navController)

        programarCheckVencimientos()
        ejecutarWorkerInmediato() // <-- PARA PROBAR AHORA MISMO
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
            15, TimeUnit.MINUTES // mínimo permitido por Android
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
        // El menú se infla automáticamente por los componentes de navegación.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}