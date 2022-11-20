package edu.uca.innovatech.cookbook.ui.view.main.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.ActivitySeeRecipeBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class SeeRecipeActivity : AppCompatActivity() {

    lateinit var receta: RecetasConPasos

    private val viewModel: RecipesViewModel by viewModels {
        RecipesViewModelFactory(
            (application as CookBookApp).database.RecetaDao()
        )
    }

    private lateinit var binding: ActivitySeeRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View Binding
        binding = ActivitySeeRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        val id = intent?.getIntExtra("id_receta", 0)

        if (id != null) {
            viewModel.agarrarReceta(id)
                .observe(this) { selectedReceta ->
                    selectedReceta?.let {
                        receta = it
                        bind(receta)
                    }
                }
        }
    }

    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            ivFotoReceta.setImageBitmap(receta.receta.bitmapImagen)
            topAppBar.title = receta.receta.nombre

            topAppBar.setNavigationOnClickListener{ onBackPressed() }
        }
    }

    private fun toggleImageView(receta: RecetasConPasos){
        binding.apply {

            ivFotoReceta.layoutParams.height = receta.receta.bitmapImagen.height
            ivFotoRecetaHolder.layoutParams.height = receta.receta.bitmapImagen.height

        }
    }
}