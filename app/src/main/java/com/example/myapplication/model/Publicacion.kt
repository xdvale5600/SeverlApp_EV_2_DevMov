package com.example.myapplication.model

data class Publicacion(
    val id: Int,
    val idProyecto: Int,
    val titulo: String,
    val autor: String,
    val anio: Int,
    val requiereToken: Boolean = true
)
