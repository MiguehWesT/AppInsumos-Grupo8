package com.example.appinsumos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinsumos.data.PedidoRepository
import kotlinx.coroutines.launch

class InsumosViewModel(private val repository: PedidoRepository) : ViewModel() {

    private val _insumoSeleccionado = MutableLiveData("")
    val insumoSeleccionado: LiveData<String> = _insumoSeleccionado

    private val _cantidad = MutableLiveData("")
    val cantidad: LiveData<String> = _cantidad

    private val _prioridad = MutableLiveData("Programada")
    val prioridad: LiveData<String> = _prioridad

    private val _mostrarConfirmacion = MutableLiveData(false)
    val mostrarConfirmacion: LiveData<Boolean> = _mostrarConfirmacion

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    val insumos = listOf(
        "Insulina",
        "Agujas",
        "Jeringas",
        "Gasas",
        "Vendas",
        "Guantes",
        "Alcohol",
        "Termómetro"
    )

    val prioridades = listOf("Urgente", "Programada")

    fun actualizarInsumo(valor: String) {
        _insumoSeleccionado.value = valor
    }

    fun actualizarCantidad(valor: String) {
        _cantidad.value = valor
    }

    fun actualizarPrioridad(valor: String) {
        _prioridad.value = valor
    }

    fun enviarSolicitud() {
        if (_insumoSeleccionado.value?.isNotEmpty() == true &&
            _cantidad.value?.isNotEmpty() == true) {
            viewModelScope.launch {
                try {
                    val exito = repository.crearPedido(
                        _insumoSeleccionado.value!!,
                        _cantidad.value!!,
                        _prioridad.value!!
                    )
                    if (exito) {
                        _mostrarConfirmacion.value = true
                        _mensaje.value = "Solicitud enviada exitosamente"
                    } else {
                        _mensaje.value = "Error al enviar la solicitud"
                    }
                } catch (e: Exception) {
                    _mensaje.value = "Error: ${e.message}"
                }
            }
        }
    }

    fun ocultarConfirmacion() {
        _mostrarConfirmacion.value = false
    }

    fun limpiarFormulario() {
        _insumoSeleccionado.value = ""
        _cantidad.value = ""
        _prioridad.value = "Programada"
    }
}