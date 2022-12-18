package edu.uca.innovatech.cookbook.data.database.entities

import androidx.room.*
import edu.uca.innovatech.cookbook.data.database.entities.Receta

@Entity(tableName = "cooking")
data class CookingReceta(
    @PrimaryKey(autoGenerate = true)
    val idCooking: Int = 0,
    @ColumnInfo(name = "id_receta")
    val idRecetaCooking: Int,
    @ColumnInfo(name = "current_step")
    val currentStep: Int = 0
)

data class CookingWReceta(
    @Embedded val cookingReceta: CookingReceta,
    @Relation(
        parentColumn = "id_receta",
        entityColumn = "id"
    )
    val receta: Receta
)
