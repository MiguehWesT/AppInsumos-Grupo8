package com.example.appinsumos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinsumos.data.UsuarioRepository

class PerfilViewModelFactory(
    private val repository: UsuarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}