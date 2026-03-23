package com.example.gestion_agil.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
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
import java.util.concurrent.TimeUnit
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        pedirPermisoNotificaciones()

        // Forma correcta de obtener el NavController con FragmentContainerView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Buscamos el drawerLayout de forma segura, ya que solo existe en tablets (layout-w600dp)
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_transform, R.id.nav_reflow, R.id.nav_profile
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView?.setupWithNavController(navController)
        
        // El NavigationView también podría estar dentro de content_main en algunos layouts (como w1240dp)
        binding.appBarMain.contentMain?.root?.findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)?.setupWithNavController(navController)

        binding.appBarMain.contentMain?.bottomNavView?.setupWithNavController(navController)

        programarCheckVencimientos()
        ejecutarWorkerInmediato()
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
        // Usamos la variable navController que inicializamos en onCreate
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}