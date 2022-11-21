package edu.uca.innovatech.cookbook.data.database.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.*
import edu.uca.innovatech.cookbook.R

@Entity(tableName = "receta")
data class Receta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "foto")
    val bitmapImagen: Bitmap,
    @ColumnInfo(name = "nombre_receta")
    val nombre: String,
    @ColumnInfo(name = "autor_receta")
    val autor: String,
    @ColumnInfo(name = "categoria")
    val categoria: String,
    @ColumnInfo(name = "tiempo_comida")
    val tiempo: String,
    @ColumnInfo(name = "tiempo_prep")
    val tiempoPrep: Int = 0,
    @ColumnInfo(name = "cant_calorias")
    val calorias: Int = 0,
    @ColumnInfo(name = "estado_pendiente")
    val isPending: Boolean = true
)

@Entity(tableName = "paso")
data class Paso(
    @PrimaryKey(autoGenerate = true)
    val idPaso: Int = 0,
    @ColumnInfo(name = "id_receta")
    val idReceta: Int,
    @ColumnInfo(name = "tiempo_paso")
    val tiempo: Int = 0,
    @ColumnInfo(name = "num_paso")
    val numPaso: Int,
    @ColumnInfo(name = "detalle_paso")
    val detalle: String = "Presione el paso que quiera para poder editar sus contenidos."
)

data class RecetasConPasos(
    @Embedded val receta: Receta,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_receta"
    )
    val pasos: List<Paso>
)
