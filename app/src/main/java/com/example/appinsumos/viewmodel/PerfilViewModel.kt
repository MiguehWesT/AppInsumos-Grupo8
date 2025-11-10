package com.example.appinsumos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinsumos.data.UsuarioRepository
import com.example.appinsumos.model.Usuario
import kotlinx.coroutines.launch

class PerfilViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuario = MutableLiveData(Usuario())
    val usuario: LiveData<Usuario> = _usuario

    private val _modoEdicion = MutableLiveData(false)
    val modoEdicion: LiveData<Boolean> = _modoEdicion

    private val _mostrarDialogoCerrarSesion = MutableLiveData(false)
    val mostrarDialogoCerrarSesion: LiveData<Boolean> = _mostrarDialogoCerrarSesion

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    init {
        cargarUsuario()
    }

    private fun cargarUsuario() {
        viewModelScope.launch {
            try {
                _usuario.value = repository.obtenerUsuario()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar usuario"
            }
        }
    }

    fun toggleModoEdicion() {
        _modoEdicion.value = !(_modoEdicion.value ?: false)
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                val exito = repository.actualizarUsuario(usuario)
                if (exito) {
                    _usuario.value = usuario
                    _modoEdicion.value = false
                    _mensaje.value = "Perfil actualizado exitosamente"
                } else {
                    _mensaje.value = "Error al actualizar perfil"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.message}"
            }
        }
    }

    fun mostrarDialogoCerrarSesion() {
        _mostrarDialogoCerrarSesion.value = true
    }

    fun ocultarDialogoCerrarSesion() {
        _mostrarDialogoCerrarSesion.value = false
    }

    fun cerrarSesion() {
        _mostrarDialogoCerrarSesion.value = false
        // Lógica para cerrar sesión
    }
}