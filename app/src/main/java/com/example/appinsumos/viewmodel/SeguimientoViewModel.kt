package com.example.appinsumos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinsumos.data.PedidoRepository
import com.example.appinsumos.model.Pedido
import kotlinx.coroutines.launch

class SeguimientoViewModel(private val repository: PedidoRepository) : ViewModel() {

    private val _pedidos = MutableLiveData<List<Pedido>>(emptyList())
    val pedidos: LiveData<List<Pedido>> = _pedidos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        cargarPedidos()
    }

    fun cargarPedidos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _pedidos.value = repository.obtenerPedidos()
            } catch (e: Exception) {
                _pedidos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obtenerPedidoPorId(id: String): Pedido? {
        return _pedidos.value?.find { it.id == id }
    }
}