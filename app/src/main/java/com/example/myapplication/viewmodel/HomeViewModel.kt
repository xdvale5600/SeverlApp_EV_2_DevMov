package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.LineaInvestigacion
import com.example.myapplication.model.Proyecto
import com.example.myapplication.repository.Repository
import kotlinx.coroutines.launch

data class HomeUiState(
    val query: String = "",
    val lineas: List<LineaInvestigacion> = Repository.lineas,
    val proyectos: List<Proyecto> = Repository.proyectos,
    val cargando: Boolean = false,
    val sinConexion: Boolean = false
)

class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var datosSolicitados = false

    fun cargarDatos() {
        if (datosSolicitados) return
        datosSolicitados = true
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true)
            val ok = Repository.cargarDesdeApi()
            uiState = filtrar(uiState.query).copy(cargando = false, sinConexion = !ok)
        }
    }

    fun onQueryChanged(query: String) {
        uiState = filtrar(query).copy(cargando = uiState.cargando, sinConexion = uiState.sinConexion)
    }

    private fun filtrar(query: String): HomeUiState {
        val termino = query.trim()
        return HomeUiState(
            query = query,
            lineas = if (termino.isEmpty()) {
                Repository.lineas
            } else {
                Repository.lineas.filter { linea ->
                    linea.titulo.contains(termino, ignoreCase = true) ||
                        linea.descripcion.contains(termino, ignoreCase = true)
                }
            },
            proyectos = Repository.buscarProyectos(query)
        )
    }
}
