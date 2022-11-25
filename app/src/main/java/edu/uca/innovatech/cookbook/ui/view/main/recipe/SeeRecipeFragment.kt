package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentSeeRecipeBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.StepsDetailsCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory
import java.math.RoundingMode
import java.text.DecimalFormat

class SeeRecipeFragment : Fragment() {

    lateinit var receta: RecetasConPasos
    lateinit var pasos: List<Paso>

    private val viewModel: RecipesViewModel by viewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database.RecetaDao()
        )
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
            tvTiempoPrepReceta.text = parseTiempoPrep(receta.receta.tiempoPrep)
            tvCaloriesReceta.text =  parseCalorias(receta.receta.calorias)

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

    //Obtiene el tiempo en minutos y lo pasa a horas si es mas de 60 minutos
    private fun parseTiempoPrep(tiempoPrep: Int): String{
        if (tiempoPrep.equals(0)){
            return "Tiempo de preparacion: Pendiente"
        } else if (tiempoPrep > 60){
            val tiempoPrepH: Double = ((tiempoPrep).toDouble())/60
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING

            return "Tiempo de preparacion: ${df.format(tiempoPrepH).toDouble()} h"
        }
        return "Tiempo de preparacion: $tiempoPrep m"
    }

    private fun parseCalorias(kcal: Int): String{
        if (kcal.equals(0))
            return "Calorias estimadas: Pendiente"

        return "Calorias estimadas: $kcal kcal"
    }

    private fun mostrarDialogConfirmacion() {
        this.context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(android.R.string.dialog_alert_title))
                .setMessage(getString(R.string.delete_recipe_dialog_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    eliminarReceta()
                }
                .show()
        }
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