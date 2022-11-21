package edu.uca.innovatech.cookbook.ui.view.main.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.ActivitySeeRecipeBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.StepsDetailsCardAdapter
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

        val adapter = StepsDetailsCardAdapter{}

        binding.rcvPasos.layoutManager = LinearLayoutManager(this)
        binding.rcvPasos.adapter = adapter

        if (id != null) {
            viewModel.agarrarReceta(id)
                .observe(this) { selectedReceta ->
                    selectedReceta?.let {
                        receta = it
                        bind(receta)
                        adapter.submitList(receta.pasos)
                    }
                }
        }
    }

    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            ivFotoReceta.setImageBitmap(receta.receta.bitmapImagen)
            topAppBar.title = receta.receta.nombre
            topAppBar.subtitle = receta.receta.autor

            topAppBar.setNavigationOnClickListener{ onBackPressed() }
            topAppBar.setOnMenuItemClickListener{ menuItem ->
                when (menuItem.itemId) {
                    R.id.editar_receta -> {

                        true
                    }
                    R.id.eliminar_receta -> {
                        mostrarDialogConfirmacion()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun mostrarDialogConfirmacion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_recipe_dialog_msg))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                eliminarReceta()
            }
            .show()
    }

    private fun eliminarReceta() {
        viewModel.deleteReceta(receta)
        onBackPressedDispatcher.onBackPressed()
    }
}