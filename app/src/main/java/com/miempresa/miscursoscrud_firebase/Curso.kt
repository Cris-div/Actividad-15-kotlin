package com.miempresa.miscursoscrud_firebase

data class Curso(
    var id: String = "",
    val userId: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val creditos: Int = 0
)