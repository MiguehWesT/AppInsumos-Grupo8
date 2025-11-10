package com.example.appinsumos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinsumos.data.PedidoRepository

class InsumosViewModelFactory(
    private val repository: PedidoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsumosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InsumosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}