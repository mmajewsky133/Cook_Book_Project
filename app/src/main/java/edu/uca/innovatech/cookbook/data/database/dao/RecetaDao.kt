package edu.uca.innovatech.cookbook.data.database.dao

import androidx.room.*
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {

    @Query("SELECT * FROM receta ORDER BY nombre_receta ASC")
    fun getRecetas():Flow<List<Receta>>

    @Query("SELECT * from receta WHERE id = :id")
    fun getReceta(id: Int): Flow<Receta>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receta: Receta)

    @Update
    suspend fun update(receta: Receta)

    @Delete
    suspend fun delete(receta: Receta)
}