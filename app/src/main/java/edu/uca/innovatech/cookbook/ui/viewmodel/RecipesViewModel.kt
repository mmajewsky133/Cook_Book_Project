package edu.uca.innovatech.cookbook.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import kotlinx.coroutines.launch

class RecipesViewModel(private val recetaDao: RecetaDao) : ViewModel() {

    //Recupera todas las recetas
    val allRecetas: LiveData<List<Receta>> = recetaDao.getRecetas().asLiveData()

    //Recupera todas las recetas dentro de un filtro y manda a llamar al query dependiendo del
    //filter puesto
    fun allRecetasFilter(filter: String, value: String): LiveData<List<Receta>> {
        if (filter == "tiempo_comida"){
            return recetaDao.getRecetasTiempo(value).asLiveData()
        }
        return recetaDao.getRecetas().asLiveData()
    }

    //Agarra una receta con pasos del dao
    fun agarrarReceta(id: Int): LiveData<RecetasConPasos> {
        return recetaDao.getRecetaConPasos(id).asLiveData()
    }

    //Recupera todas los pasos de una receta
    fun agarrarPasos(id: Int): LiveData<List<Paso>> = recetaDao.getPasos(id).asLiveData()

    //Recupera un paso de una receta
    fun agarrarPaso(id: Int, idReceta: Int): LiveData<Paso> {
        return recetaDao.getPaso(id, idReceta).asLiveData()
    }

    //Recupera todas los ingredientes de una receta
    fun agarrarIngredientes(id: Int): LiveData<List<Ingrediente>> =
        recetaDao.getIngredientes(id).asLiveData()

    //Recupera un ingrediente de una receta
    fun agarrarIngrediente(id: Int, idReceta: Int): LiveData<Ingrediente> {
        return recetaDao.getIngrediente(id, idReceta).asLiveData()
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

    fun actualizarReceta(
        idReceta: Int, imagen: Bitmap, nombre: String, autor: String, categoria: String,
        tiempo: String
    ) {
        val editedReceta = Receta(
            id = idReceta,
            bitmapImagen = imagen,
            nombre = nombre,
            autor = autor,
            categoria = categoria,
            tiempo = tiempo,
            isPending = false
        )
        updateReceta(editedReceta)
    }

    fun actualizarRecetaEstado(receta: RecetasConPasos) {
        val recetaUpdated = receta.receta
        val pasosReceta = receta.pasos
        var tiempoPrepReceta = receta.receta.tiempoPrepPrep

        //Recorre los pasos de la receta y suma todos los tiempos para dar
        //El total de tiempo de preparacion
        for (tiempo in pasosReceta) {
            tiempoPrepReceta += tiempo.tiempo
        }

        recetaUpdated.isPending = false
        recetaUpdated.tiempoPrep = tiempoPrepReceta

        updateReceta(recetaUpdated)
    }

    fun guardarCambiosTiempoPrepPrep(receta: RecetasConPasos, tiempoPrepPrep: Int) {
        val recetaUpdated = receta.receta

        recetaUpdated.tiempoPrepPrep = tiempoPrepPrep

        updateReceta(recetaUpdated)
    }

    private fun updateReceta(receta: Receta) {
        viewModelScope.launch {
            recetaDao.updateReceta(receta)
        }
    }

    //Manda a llamar el Dao para eliminar una receta
    fun deleteReceta(receta: RecetasConPasos) {
        viewModelScope.launch {
            recetaDao.deleteIngredientes(receta.receta.id)
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

    fun guardarCambiosPaso(
        id: Int,
        idReceta: Int,
        numPaso: Int,
        imagen: Bitmap,
        tiempoPrep: Int,
        detallePaso: String
    ) {
        val nuevoPaso = Paso(
            idPaso = id,
            idReceta = idReceta,
            numPaso = numPaso,
            imagenPaso = imagen,
            tiempo = tiempoPrep,
            detalle = detallePaso
        )
        updatePaso(nuevoPaso)
    }

    private fun updatePaso(paso: Paso) {
        viewModelScope.launch {
            recetaDao.updatePaso(paso)
        }
    }

    fun agregarNuevoIngrediente(id: Int) {
        val nuevoIngrediente =
            Ingrediente(
                idReceta = id
            )
        insertIngrediente(nuevoIngrediente)
    }

    private fun insertIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            recetaDao.insertIngrediente(ingrediente)
        }
    }

    fun guardarCambiosIngrediente(
        id: Int,
        idReceta: Int,
        nombre: String,
        cant: Int,
        medida: String,
        kcals: Int
    ) {
        val nuevoIngrediente =
            Ingrediente(
                idIngrediente = id,
                idReceta = idReceta,
                nombreIngrediente = nombre,
                cantIngrediente = cant,
                medidaIngrediente = medida,
                caloriasIngrediente = kcals
            )
        updateIngrediente(nuevoIngrediente)
    }

    private fun updateIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            recetaDao.updateIngrediente(ingrediente)
        }
    }

    fun eliminarIngrediente(id: Int, idReceta: Int) {
        deleteIngrediente(id, idReceta)
    }

    private fun deleteIngrediente(id: Int, idReceta: Int) {
        viewModelScope.launch {
            recetaDao.deleteIngrediente(id, idReceta)
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