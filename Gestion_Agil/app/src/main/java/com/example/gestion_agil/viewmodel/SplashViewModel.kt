    package com.example.gestion_agil.viewmodel

    import android.app.Application
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.viewModelScope
    import com.example.gestion_agil.data.model.AppDatabase
    import com.example.gestion_agil.data.repository.SesionRepository
    import kotlinx.coroutines.launch

    class SplashViewModel(application: Application) : AndroidViewModel(application) {

        private val sesionRepository: SesionRepository

        // LiveData que el Splash observará
        private val _sesionActiva = MutableLiveData<Int?>()
        val sesionActiva: LiveData<Int?> get() = _sesionActiva

        private val _cargando = MutableLiveData(true)
        val cargando: LiveData<Boolean> get() = _cargando

        init {
            val sesionDao = AppDatabase.getDatabase(application).SesionDao()
            sesionRepository = SesionRepository(sesionDao)
        }

        fun verificarSesion() {
            viewModelScope.launch {
                val sesion = sesionRepository.obtenerSesion()
                _sesionActiva.postValue(sesion?.id_usuario)
                _cargando.postValue(false)   // ← Splash puede desaparecer
            }
        }
    }