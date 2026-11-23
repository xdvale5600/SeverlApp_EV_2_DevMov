package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.Repository

data class IndicadoresUiState(
    val proyectosPorLinea: List<Pair<String, Int>>,
    val proyectosPorEstado: List<Pair<String, Int>>
)

class IndicadoresViewModel : ViewModel() {
    var uiState by mutableStateOf(calcular())
        private set

    fun actualizar() {
        uiState = calcular()
    }

    private fun calcular(): IndicadoresUiState = IndicadoresUiState(
        proyectosPorLinea = Repository.lineas.map { linea ->
            val investigadorIds = Repository.getInvestigadoresByLinea(linea.id).map { it.id }
            val cantidad = Repository.proyectos.count { it.idInvestigador in investigadorIds }
            linea.titulo.substringBefore(" ") to cantidad
        },
        proyectosPorEstado = Repository.proyectos
            .groupBy { it.estado }
            .map { (estado, proyectos) -> estado to proyectos.size }
    )
}
