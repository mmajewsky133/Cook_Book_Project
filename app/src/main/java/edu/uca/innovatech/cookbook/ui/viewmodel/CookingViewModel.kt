package edu.uca.innovatech.cookbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.uca.innovatech.cookbook.data.database.dao.CocinandoDao
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao

class CookingViewModel(private val recetaDao: RecetaDao, private val cocinandoDao: CocinandoDao) :
    ViewModel() {

}

//Clase Factory para instansear la instanciade ViewModel
class CookingViewModelFactory(private val recetaDao: RecetaDao, private val cocinandoDao: CocinandoDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CookingViewModel(recetaDao, cocinandoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}