package edu.uca.innovatech.cookbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.data.database.dao.CocinandoDao
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao

class CookingViewModel(private val recetaDao: RecetaDao, private val cocinandoDao: CocinandoDao) :
    ViewModel() {



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