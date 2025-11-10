package com.example.appinsumos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinsumos.data.PedidoRepository

class SeguimientoViewModelFactory(
    private val repository: PedidoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeguimientoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SeguimientoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}