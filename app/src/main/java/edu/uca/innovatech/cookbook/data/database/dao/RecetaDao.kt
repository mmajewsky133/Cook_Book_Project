package edu.uca.innovatech.cookbook.data.database.dao

import androidx.room.*
import edu.uca.innovatech.cookbook.data.database.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {

    //Queries para obtener datos generales de una receta
    @Query("SELECT * FROM receta ORDER BY nombre_receta ASC")
    fun getRecetas(): Flow<List<Receta>>

    @Query("SELECT * from receta WHERE id = :id")
    fun getReceta(id: Int): Flow<Receta>

    //Queries para obtener datos de pasos especificos
    @Query("SELECT * FROM paso WHERE id_receta = :idReceta ORDER BY num_paso ASC")
    fun getPasos(idReceta: Int): Flow<List<Paso>>

    @Query("SELECT * from paso WHERE idPaso = :idPaso AND id_receta = :idReceta")
    fun getPaso(idPaso: Int, idReceta: Int): Flow<Paso>

    //Query para obtener una receta con sus pasos
    @Transaction
    @Query("SELECT * FROM receta WHERE id = :idReceta")
    fun getRecetaConPasos(idReceta: Int): Flow<RecetasConPasos>

    //Insert, Update y Delete de una receta
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReceta(receta: Receta): Long

    @Update
    suspend fun updateReceta(receta: Receta)

    @Delete()
    suspend fun deleteReceta(receta: Receta)

    //Insert, Update y Delete de un paso de una receta
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPaso(paso: Paso)

    @Update
    suspend fun updatePaso(paso: Paso)

    //El Delete es un query para eliminar un paso de una receta
    @Query("DELETE FROM paso WHERE idPaso = :idPaso AND id_receta = :idReceta")
    suspend fun deletePaso(idPaso: Int, idReceta: Int)

    //El Delete es un query para poder especificar que pasos con que id de receta se borraran
    @Query("DELETE FROM paso WHERE id_receta = :idReceta")
    suspend fun deletePasos(idReceta: Int)
}