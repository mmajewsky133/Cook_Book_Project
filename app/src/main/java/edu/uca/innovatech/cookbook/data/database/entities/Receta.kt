package edu.uca.innovatech.cookbook.data.database.entities

import android.graphics.Bitmap
import androidx.room.*

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
    var tiempoPrep: Int = 0,
    @ColumnInfo(name = "tiempo_prep_prep")
    var tiempoPrepPrep: Int = 0,
    @ColumnInfo(name = "cant_calorias")
    var calorias: Int = 0,
    @ColumnInfo(name = "estado_pendiente")
    var isPending: Boolean = true
)

@Entity(tableName = "paso")
data class Paso(
    @PrimaryKey(autoGenerate = true)
    val idPaso: Int = 0,
    @ColumnInfo(name = "id_receta")
    val idReceta: Int,
    @ColumnInfo(name = "img_paso")
    val imagenPaso: Bitmap = Bitmap.createBitmap(1284, 1247, Bitmap.Config.ARGB_8888),
    @ColumnInfo(name = "tiempo_paso")
    val tiempo: Int = 1,
    @ColumnInfo(name = "num_paso")
    val numPaso: Int,
    @ColumnInfo(name = "detalle_paso")
    val detalle: String = "Presione el paso que quiera para poder editar sus contenidos."
)

@Entity(tableName = "ingrediente")
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    val idIngrediente: Int = 0,
    @ColumnInfo(name = "id_receta")
    val idReceta: Int,
    @ColumnInfo(name = "nombre_ingrediente")
    val nombreIngrediente: String = "Ingrediente nuevo",
    @ColumnInfo(name = "cant_ingrediente")
    val cantIngrediente: Int = 0,
    @ColumnInfo(name = "medida_ingrediente")
    val medidaIngrediente: String = "xxx",
    @ColumnInfo(name = "cant_calorias_ingrediente")
    var caloriasIngrediente: Int = 0,
)

data class RecetasConPasos(
    @Embedded val receta: Receta,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_receta"
    )
    val ingredientes: List<Ingrediente>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_receta"
    )
    val pasos: List<Paso>
)
