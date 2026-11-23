package com.example.myapplication.remote

import com.example.myapplication.model.Investigador
import com.example.myapplication.model.LineaInvestigacion
import com.example.myapplication.model.Proyecto
import com.example.myapplication.model.Publicacion
import retrofit2.http.GET

interface ApiService {
    @GET("lineas")
    suspend fun getLineas(): List<LineaInvestigacion>

    @GET("investigadores")
    suspend fun getInvestigadores(): List<Investigador>

    @GET("proyectos")
    suspend fun getProyectos(): List<Proyecto>

    @GET("publicaciones")
    suspend fun getPublicaciones(): List<Publicacion>
}
