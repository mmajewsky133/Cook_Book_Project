package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDetailBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.view.adapter.StepsDetailsCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class AddRecipeDetailFragment : Fragment() {

    private val navigationArgs: AddRecipeDataFragmentArgs by navArgs()
    lateinit var receta: RecetasConPasos
    var pasosCount: Int = 0


    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database
                .RecetaDao()
        )
    }

    private var _binding: FragmentAddRecipeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        //Conseguir id del navigation args
        val id = navigationArgs.recetaId

        //Atraves del id se obtiene los datos generales de la receta recien guardada
        // mas los pasos (vacios)
        viewModel.agarrarReceta(id).observe(this.viewLifecycleOwner) { selectedItem ->
            receta = selectedItem
            bind(receta)
        }

        val adapter = StepsDetailsCardAdapter {
            //Codigo para cuando se presione en el CardView

        }

        binding.rcvPasos.layoutManager = LinearLayoutManager(this.context)
        binding.rcvPasos.adapter = adapter

        viewModel.agarrarPasos(id).observe(this.viewLifecycleOwner) { pasos ->
            pasos.let {
                adapter.submitList(it)
            }
        }

        binding.btnAddPaso.setOnClickListener{
            agregarPaso()
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            topAppBar.title = receta.receta.nombre
            topAppBar.subtitle = receta.receta.autor

            topAppBar.setNavigationOnClickListener{ mostrarDialogConfirmacionSalida() }
        }
    }

    private fun agregarPaso(){
        pasosCount++
        if (pasosCount<4)
            viewModel.agregarNuevoPaso(receta.receta.id, pasosCount)
        else if (pasosCount==4) {
            viewModel.agregarNuevoPaso(receta.receta.id, pasosCount)
            binding.btnAddPaso.isEnabled = false
        }
    }


    private fun mostrarDialogConfirmacionSalida() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(android.R.string.dialog_alert_title))
                .setMessage(getString(R.string.delete_recipe_dialog_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    //Codigo
                }
                .show()
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}