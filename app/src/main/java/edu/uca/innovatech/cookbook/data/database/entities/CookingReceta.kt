package edu.uca.innovatech.cookbook.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cooking")
data class CookingReceta(
    @PrimaryKey(autoGenerate = true)
    val idCooking: Int = 0,
    @ColumnInfo(name = "id_receta")
    val idRecetaCooking: Int,
    @ColumnInfo(name = "current_step")
    val currentStep: Int = 0
)
