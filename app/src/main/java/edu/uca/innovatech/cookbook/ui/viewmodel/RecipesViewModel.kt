package edu.uca.innovatech.cookbook.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import kotlinx.coroutines.launch

class RecipesViewModel(private val recetaDao: RecetaDao) : ViewModel() {

    //Recupera todas las recetas
    val allRecetas: LiveData<List<Receta>> = recetaDao.getRecetas().asLiveData()

    //Agarra una receta con pasos del dao
    fun agarrarReceta(id: Int): LiveData<RecetasConPasos> {
        return recetaDao.getRecetaConPasos(id).asLiveData()
    }

    //crea un objeto de tipo Receta para mandar a guardar tal objeto
    fun agregarReceta(
        imagen: Bitmap, nombre: String, autor: String, categoria: String,
        tiempo: String, pasos: Int
    ) {
        val nuevaReceta = Receta(
            bitmapImagen = imagen,
            nombre = nombre,
            autor = autor,
            categoria = categoria,
            tiempo = tiempo,
            pasos = pasos
        )
        insertReceta(nuevaReceta)
    }

    //Manda a llamar al Dao para guardar la receta
    private fun insertReceta(receta: Receta) {
        viewModelScope.launch {
            recetaDao.insertReceta(receta)
        }
    }

    //Manda a llamar el Dao para eliminar una receta
    fun deleteReceta(receta: RecetasConPasos) {
        viewModelScope.launch {
            recetaDao.deletePasos(receta.receta.id)
            recetaDao.deleteReceta(receta.receta)
        }
    }

    //Estoy tan cansado que estoy dispuesto a hacer esto
    //Basicamente dependiendo de la opcion de usuario devuelve el Int correspondiente
    fun pasosConverter(pasos: String): Int{

        var numPasos: Int = 0

        when (pasos){
            "1 Paso" -> numPasos = 1
            "2 Pasos" -> numPasos = 2
            "3 Pasos" -> numPasos = 3
            "4 Pasos" -> numPasos = 4
            "5 Pasos" -> numPasos = 5
            "6 Pasos" -> numPasos = 6
            "7 Pasos" -> numPasos = 7
            "8 Pasos" -> numPasos = 8
            "9 Pasos" -> numPasos = 9
            "10 Pasos" -> numPasos = 10
        }
        return numPasos
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