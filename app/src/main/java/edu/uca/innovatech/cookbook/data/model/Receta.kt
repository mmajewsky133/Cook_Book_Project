package edu.uca.innovatech.cookbook.data.model

import androidx.annotation.DrawableRes

data class Receta(
    @DrawableRes val imageResourceId: Int,
    val nombre: String,
    val autor: String,
    val categoria: Int,
    val tiempo: Int,
    val medida: Int,
    val pasos: Int,
    val calorias: Int
)
