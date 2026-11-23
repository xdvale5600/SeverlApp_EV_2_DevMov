package com.example.myapplication.repository

import com.example.myapplication.remote.RetrofitInstance
import com.example.myapplication.model.LineaInvestigacion
import com.example.myapplication.model.Investigador
import com.example.myapplication.model.Proyecto
import com.example.myapplication.model.Publicacion

object Repository {

    @Volatile
    var lineas: List<LineaInvestigacion> = listOf(
        LineaInvestigacion(
            id = 1,
            titulo = "Bioproductos y Biorrefinería",
            descripcion = "Desarrollo de materiales sustentables, adhesivos verdes y compuestos de alto valor derivados de biomasa forestal."
        ),
        LineaInvestigacion(
            id = 2,
            titulo = "Edificación de Alto Rendimiento en Madera",
            descripcion = "Innovación en sistemas constructivos industrializados, Madera Contralaminada (CLT) y comportamiento estructural."
        ),
        LineaInvestigacion(
            id = 3,
            titulo = "Gestión e Inteligencia Forestal Sustentable",
            descripcion = "Monitoreo mediante sensores, modelamiento de crecimiento y optimización de la cadena de suministro forestal."
        )
    )
        private set

    @Volatile
    var investigadores: List<Investigador> = listOf(
        Investigador(id = 1, idLinea = 1, nombre = "Dra. Sofía Soto", especialidad = "Química de Polímeros y Lignina"),
        Investigador(id = 2, idLinea = 1, nombre = "Dr. Marcelo Tapia", especialidad = "Biomateriales e Nano-celulosa"),
        Investigador(id = 3, idLinea = 2, nombre = "Ing. Carlos Mendoza", especialidad = "Ingeniería Estructural y Paneles CLT"),
        Investigador(id = 4, idLinea = 2, nombre = "Dra. Valentina Rojas", especialidad = "Eficiencia Energética y Hábitat"),
        Investigador(id = 5, idLinea = 3, nombre = "Dr. Esteban Silva", especialidad = "Geoinformática y Biomasa Forestal")
    )
        private set

    @Volatile
    var proyectos: List<Proyecto> = listOf(
        Proyecto(id = 1, idInvestigador = 1, titulo = "Proyecto Alpha: Resinas Ecológicas libres de Formaldehído", anio = 2024, estado = "En Ejecución"),
        Proyecto(id = 2, idInvestigador = 2, titulo = "Aislamiento Térmico mediante Aerogeles de Celulosa", anio = 2023, estado = "Finalizado"),
        Proyecto(id = 3, idInvestigador = 3, titulo = "Sistema de Conexiones Sísmicas para Edificios de Madera en Altura", anio = 2024, estado = "En Ejecución"),
        Proyecto(id = 4, idInvestigador = 4, titulo = "Prototipo de Vivienda Social Sustentable de Huella Cero", anio = 2022, estado = "Finalizado"),
        Proyecto(id = 5, idInvestigador = 5, titulo = "Sistema EcoForest: Monitoreo Satelital de Captura de Carbono", anio = 2023, estado = "En Ejecución")
    )
        private set

    @Volatile
    var publicaciones: List<Publicacion> = listOf(
        Publicacion(id = 1, idProyecto = 1, titulo = "Informe Técnico: Evaluación de Resinas a Base de Lignina", autor = "Dra. Sofía Soto", anio = 2024, requiereToken = true),
        Publicacion(id = 2, idProyecto = 3, titulo = "Manual de Diseño Estructural en Madera Contralaminada (CLT)", autor = "Ing. Carlos Mendoza", anio = 2024, requiereToken = true),
        Publicacion(id = 3, idProyecto = 5, titulo = "Guía Metodológica de Estimación de Biomasa con Datos LIDAR", autor = "Dr. Esteban Silva", anio = 2023, requiereToken = false)
    )
        private set

    suspend fun cargarDesdeApi(): Boolean = try {
        val api = RetrofitInstance.api
        val nuevasLineas = api.getLineas()
        val nuevosInvestigadores = api.getInvestigadores()
        val nuevosProyectos = api.getProyectos()
        val nuevasPublicaciones = api.getPublicaciones()
        lineas = nuevasLineas
        investigadores = nuevosInvestigadores
        proyectos = nuevosProyectos
        publicaciones = nuevasPublicaciones
        true
    } catch (e: Exception) {
        false
    }

    fun getLineaById(id: Int): LineaInvestigacion? = lineas.find { it.id == id }
    fun getInvestigadoresByLinea(idLinea: Int): List<Investigador> = investigadores.filter { it.idLinea == idLinea }
    fun getInvestigadorById(id: Int): Investigador? = investigadores.find { it.id == id }
    fun getProyectosByInvestigador(idInvestigador: Int): List<Proyecto> = proyectos.filter { it.idInvestigador == idInvestigador }
    fun getProyectoById(id: Int): Proyecto? = proyectos.find { it.id == id }
    fun getPublicacionesByProyecto(idProyecto: Int): List<Publicacion> = publicaciones.filter { it.idProyecto == idProyecto }

    fun buscarProyectos(query: String): List<Proyecto> {
        val termino = query.trim()
        if (termino.isEmpty()) return proyectos

        return proyectos.filter {
            val investigador = getInvestigadorById(it.idInvestigador)
            val linea = investigador?.let { persona -> getLineaById(persona.idLinea) }

            listOf(
                it.titulo,
                it.anio.toString(),
                it.estado,
                investigador?.nombre,
                investigador?.especialidad,
                linea?.titulo,
                linea?.descripcion
            ).any { campo -> campo?.contains(termino, ignoreCase = true) == true }
        }
    }
}