package edu.uca.innovatech.cookbook.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import kotlinx.coroutines.launch

class RecipesViewModel(private val recetaDao: RecetaDao) : ViewModel() {

    val allRecetas: LiveData<List<Receta>> = recetaDao.getRecetas().asLiveData()

    fun agarrarReceta(id: Int): LiveData<Receta> {
        return recetaDao.getReceta(id).asLiveData()
    }

    fun agregarReceta(
        imagen: Uri, nombre: String, autor: String, categoria: String,
        tiempo: String, pasos: Int
    ) {
        val nuevaReceta = Receta(
            uriImagen = imagen,
            nombre = nombre,
            autor = autor,
            categoria = categoria,
            tiempo = tiempo,
            pasos = pasos
        )
        insertReceta(nuevaReceta)
    }

    private fun insertReceta(receta: Receta) {
        viewModelScope.launch {
            recetaDao.insert(receta)
        }
    }

    //Estoy tan cansado que estoy dispuesto a hacer esto
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