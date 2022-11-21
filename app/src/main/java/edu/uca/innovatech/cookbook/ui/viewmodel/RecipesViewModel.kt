package edu.uca.innovatech.cookbook.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class RecipesViewModel(private val recetaDao: RecetaDao) : ViewModel() {

    //Recupera todas las recetas
    val allRecetas: LiveData<List<Receta>> = recetaDao.getRecetas().asLiveData()

    //Agarra una receta con pasos del dao
    fun agarrarReceta(id: Int): LiveData<RecetasConPasos> {
        return recetaDao.getRecetaConPasos(id).asLiveData()
    }

    fun agarrarPasos(id: Int): LiveData<List<Paso>> {
        return recetaDao.getPasos(id).asLiveData()
    }

    private fun agarrarPasosArray(id: Int): List<Paso>? {
        return recetaDao.getPasos(id).asLiveData().value
    }

    //crea un objeto de tipo Receta para mandar a guardar tal objeto
    suspend fun agregarReceta(
        imagen: Bitmap, nombre: String, autor: String, categoria: String,
        tiempo: String
    ): Int {
        val nuevaReceta = Receta(
            bitmapImagen = imagen,
            nombre = nombre,
            autor = autor,
            categoria = categoria,
            tiempo = tiempo
        )
        return insertReceta(nuevaReceta)
    }

    //Manda a llamar al Dao para guardar la receta
    private suspend fun insertReceta(receta: Receta): Int {
        return recetaDao.insertReceta(receta).toString().toInt()
    }

    fun actualizarRecetaEstado(receta: Receta){
        val recetaUpdated = receta

        recetaUpdated.isPending = false

        updateReceta(recetaUpdated)
    }

    private fun updateReceta(receta: Receta){
        viewModelScope.launch {
            recetaDao.updateReceta(receta)
        }
    }

    //Manda a llamar el Dao para eliminar una receta
    fun deleteReceta(receta: RecetasConPasos) {
        viewModelScope.launch {
            recetaDao.deletePasos(receta.receta.id)
            recetaDao.deleteReceta(receta.receta)
        }
    }

    //Crea un objeto de tipo Paso y asigna el numero del paso
    fun agregarNuevoPaso(id: Int, num: Int) {
        val nuevoPaso =
            Paso(
                idReceta = id,
                numPaso = num
            )
        insertPaso(nuevoPaso)
    }

    //Manda a insertar el Paso nuevo
    private fun insertPaso(paso: Paso) {
        viewModelScope.launch {
            recetaDao.insertPaso(paso)
        }
    }
}

//Clase Factory para instansear la instanciade ViewModel
class RecipesViewModelFactory(private val recetaDao: RecetaDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipesViewModel(recetaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}