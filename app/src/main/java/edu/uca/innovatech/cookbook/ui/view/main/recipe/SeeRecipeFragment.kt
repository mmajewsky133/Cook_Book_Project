package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.core.ex.showMaterialDialog
import edu.uca.innovatech.cookbook.core.util.parseCalorias
import edu.uca.innovatech.cookbook.core.util.parseIngredientes
import edu.uca.innovatech.cookbook.core.util.parseTiempoPrep
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentSeeRecipeBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.StepsDetailsCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel

class SeeRecipeFragment : Fragment() {

    lateinit var receta: RecetasConPasos
    lateinit var pasos: List<Paso>

    private val viewModel: RecipesViewModel by viewModels {
        RecipesViewModel.factory
    }

    private var _binding: FragmentSeeRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeeRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val id = activity?.intent?.getIntExtra("id_receta", 0)

        val adapter = StepsDetailsCardAdapter {}

        binding.rcvPasos.layoutManager = LinearLayoutManager(this.context)
        binding.rcvPasos.adapter = adapter

        if (id != null) {
            viewModel.agarrarReceta(id)
                .observe(this.viewLifecycleOwner) { selectedReceta ->
                    selectedReceta?.let {
                        bind(it)
                        pasos = it.pasos
                        adapter.submitList(pasos)
                    }
                }
        }
    }

    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            topAppBar.title = receta.receta.nombre
            topAppBar.subtitle = "Escrita por: ${receta.receta.autor}"

            ivFotoReceta.setImageBitmap(receta.receta.bitmapImagen)
            tvCategoriaReceta.text =
                getString(R.string.placeholderText_CatReceta, receta.receta.categoria)
            tvTiempoComidaReceta.text =
                getString(R.string.placeholderText_TiempoComidaReceta, receta.receta.tiempo)
            tvTiempoPrepReceta.text = parseTiempoPrep(receta.receta.tiempoPrep)
            tvCaloriesReceta.text = parseCalorias(receta.receta.calorias)

            tvTiempoPrepPreparacion.text = parseTiempoPrep(receta.receta.tiempoPrepPrep)
            tvIngrediente.text = parseIngredientes(receta.ingredientes)
            tvIngrediente.isVisible = true

            topAppBar.setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editar_receta -> {
                        editarReceta(receta.receta.id)
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
        showMaterialDialog(
            getString(android.R.string.dialog_alert_title),
            getString(R.string.delete_recipe_dialog_msg),
            false,
            getString(R.string.no),
            getString(R.string.yes),
            {}, { eliminarReceta() }
        )
    }

    private fun editarReceta(idReceta: Int) {
        val action = SeeRecipeFragmentDirections.actionSeeRecipeFragmentToNavGraphNewRecipes(
            idReceta
        )
        findNavController().navigate(action)
    }

    private fun eliminarReceta() {
        val id = activity?.intent?.getIntExtra("id_receta", 0)

        if (id != null) {
            viewModel.agarrarReceta(id)
                .observe(this.viewLifecycleOwner) { selectedReceta ->
                    selectedReceta?.let {
                        receta = it
                        viewModel.deleteReceta(receta)
                    }
                }
        }
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}