package com.example.gestion_agil.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestion_agil.data.repository.NotificacionRepository

class SlideShowViewModelFactory(
    private val repository: NotificacionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideshowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlideshowViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}