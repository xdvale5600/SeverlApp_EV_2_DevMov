package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Investigador
import com.example.myapplication.model.LineaInvestigacion
import com.example.myapplication.model.Proyecto
import com.example.myapplication.repository.Repository

data class DetalleLineaUiState(
    val linea: LineaInvestigacion?,
    val investigadores: List<Investigador>,
    val proyectos: List<Proyecto>
)

class DetalleLineaViewModel : ViewModel() {
    fun load(idLinea: Int): DetalleLineaUiState {
        val investigadores = Repository.getInvestigadoresByLinea(idLinea)
        val investigadorIds = investigadores.mapTo(HashSet()) { it.id }
        return DetalleLineaUiState(
            linea = Repository.getLineaById(idLinea),
            investigadores = investigadores,
            proyectos = Repository.proyectos.filter { it.idInvestigador in investigadorIds }
        )
    }
}
