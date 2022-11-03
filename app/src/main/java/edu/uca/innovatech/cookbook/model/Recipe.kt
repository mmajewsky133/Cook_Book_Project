package edu.uca.innovatech.cookbook.model

data class Recipe(
    val nombre: String,
    val autor: String,
    val categoria: Int,
    val tiempo: Int,
    val medida: Int,
    val pasos: Int
)
