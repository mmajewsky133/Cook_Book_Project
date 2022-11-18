package edu.uca.innovatech.cookbook.data.database.entities

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Receta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "foto")
    val uriImagen: Uri,
    @ColumnInfo(name = "nombre_receta")
    val nombre: String,
    @ColumnInfo(name = "autor_receta")
    val autor: String,
    @ColumnInfo(name = "categoria")
    val categoria: String,
    @ColumnInfo(name = "tiempo")
    val tiempo: String,
    @ColumnInfo(name = "num_pasos")
    val pasos: Int,
    @ColumnInfo(name = "cant_calorias")
    val calorias: Int = 0
)
