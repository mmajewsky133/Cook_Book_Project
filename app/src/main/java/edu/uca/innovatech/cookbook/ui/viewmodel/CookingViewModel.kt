package edu.uca.innovatech.cookbook.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.data.database.dao.CocinandoDao
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos

class CookingViewModel(private val recetaDao: RecetaDao, private val cocinandoDao: CocinandoDao) :
    ViewModel() {

    //Recupera todas las recetas a cocinar
    val allToCookingRecetas: LiveData<List<CookingReceta>> =
        cocinandoDao.getToCookRecipes().asLiveData()

    fun agarrarToCookReceta(id: Int): LiveData<CookingReceta>{
        return cocinandoDao.getToCookRecipe(id).asLiveData()
    }

    //Agarra una receta con pasos del dao
    fun agarrarReceta(id: Int): LiveData<RecetasConPasos> {
        return recetaDao.getRecetaConPasos(id).asLiveData()
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            addInitializer(CookingViewModel::class) {
                CookingViewModel(
                    CookBookApp.database.RecetaDao(),
                    CookBookApp.database.CocinandoDao()
                )
            }
            build()
        }
    }
}