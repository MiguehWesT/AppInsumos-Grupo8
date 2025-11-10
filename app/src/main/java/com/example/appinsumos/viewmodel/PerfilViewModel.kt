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

    fun mostrarMensaje(texto: String) {
        _mensaje.value = texto
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

    // ------------------------------------------------------------
    // 📸 NUEVAS FUNCIONES: foto de perfil y ubicación GPS
    // ------------------------------------------------------------

    /** Guarda la ruta URI de la foto tomada con la cámara */
    fun guardarFotoPerfil(uri: String) {
        val usuarioActual = _usuario.value ?: Usuario()
        _usuario.value = usuarioActual.copy(fotoUri = uri)

        // (Opcional) Guardar también en BD
        viewModelScope.launch {
            try {
                repository.actualizarUsuario(_usuario.value!!)
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar foto: ${e.message}"
            }
        }
    }

    /** Guarda la ubicación actual del usuario */
    fun guardarUbicacion(ubicacion: String) {
        val usuarioActual = _usuario.value ?: Usuario()
        _usuario.value = usuarioActual.copy(ubicacion = ubicacion)

        // (Opcional) Persistir en la base de datos
        viewModelScope.launch {
            try {
                repository.actualizarUsuario(_usuario.value!!)
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar ubicación: ${e.message}"
            }
        }
    }
}
