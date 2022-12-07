package edu.uca.innovatech.cookbook.data.database.dao

import androidx.room.*
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.CookingWReceta
import kotlinx.coroutines.flow.Flow

@Dao
interface CocinandoDao {
    //Query para obtener lista de recetas a cocinar
    @Query("SELECT * FROM cooking")
    fun getToCookRecipes(): Flow<List<CookingWReceta>>

    @Query("SELECT * FROM cooking WHERE idCooking = :id")
    fun getToCookRecipe(id: Int): Flow<CookingReceta>

    //Insert, Update y Delete de una receta a cocinar
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCooking(cookingReceta: CookingReceta)

    @Update
    suspend fun updateCooking(cookingReceta: CookingReceta)

    @Delete()
    suspend fun deleteCooking(cookingReceta: CookingReceta)
}