package edu.uca.innovatech.cookbook.ui.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.data.database.dao.CocinandoDao
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.CookingWReceta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import kotlinx.coroutines.launch

class CookingViewModel(private val recetaDao: RecetaDao, private val cocinandoDao: CocinandoDao) :
    ViewModel() {

    //Recupera todas las recetas a cocinar
    val allToCookingRecetas: LiveData<List<CookingWReceta>> =
        cocinandoDao.getToCookRecipes().asLiveData()

    fun agarrarToCookReceta(id: Int): LiveData<CookingReceta>{
        return cocinandoDao.getToCookRecipe(id).asLiveData()
    }

    //Agarra una receta con pasos del dao
    fun agarrarReceta(id: Int): LiveData<RecetasConPasos> {
        return recetaDao.getRecetaConPasos(id).asLiveData()
    }

    //crea un objeto de tipo Receta para mandar a guardar tal objeto
    fun agregarCocinar(idReceta: Int) {
        val nuevoCocinar = CookingReceta(idRecetaCooking = idReceta)
        insertCocinar(nuevoCocinar)
    }

    //Manda a llamar al Dao para guardar la receta a cocinar
    private fun insertCocinar(cookingReceta: CookingReceta){
        viewModelScope.launch {
            cocinandoDao.insertCooking(cookingReceta)
        }
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