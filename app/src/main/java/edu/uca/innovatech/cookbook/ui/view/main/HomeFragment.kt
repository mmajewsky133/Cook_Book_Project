package edu.uca.innovatech.cookbook.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.core.ex.showMaterialDialog
import edu.uca.innovatech.cookbook.core.ex.showToast
import edu.uca.innovatech.cookbook.databinding.FragmentHomeBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeSelectionCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.CookingViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import java.util.*


class HomeFragment : Fragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModelRecipes: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
    }
    private val viewModelCooking: CookingViewModel by activityViewModels {
        CookingViewModel.factory
    }

    // El ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        var tiempo = ""

        bind(currentHour)

        val adapter = RecipeSelectionCardAdapter {
            //Codigo para cuando se presione en el boton CardView
            mostrarDialogConfirmacion(it.id)
        }
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        binding.rcvListaRecetas.layoutManager = layoutManager
        binding.rcvListaRecetas.adapter = adapter

        if (currentHour in 5..10) {
            tiempo = "Desayuno"
        } else if (currentHour in 11..16) {
            tiempo = "Almuerzo"
        } else if (currentHour in 18..22) {
            tiempo = "Cena"
        } else {
            tiempo = "Snack"
        }

        viewModelRecipes.allRecetasFilter("tiempo_comida", tiempo)
            .observe(this.viewLifecycleOwner) { recetas ->
                recetas.let {
                    adapter.submitList(it)
                }
            }

        binding.fabNuevaAccion.setOnClickListener {
            //TODO Implement Speed Dial for: Start Cooking, Add Recipe, Add consumed food
        }
    }

    private fun bind(currentHour: Int) {
        if (currentHour in 4..12) {
            binding.tvTiempoDia.text = getString(R.string.good_morning_label)
        } else if (currentHour in 13..18) {
            binding.tvTiempoDia.text = getString(R.string.good_afternoon_label)
        } else {
            binding.tvTiempoDia.text = getString(R.string.good_evening_label)
        }
    }

    private fun mostrarDialogConfirmacion(id: Int) {
        showMaterialDialog(
            getString(R.string.start_cooking), getString(R.string.cook_recipe_dialog_msg),
            false, getString(R.string.cancel), getString(R.string.ok),
            {}, {
                viewModelCooking.agregarCocinar(id)
                showToast("La receta esta lista en el apartado Cocinando", Toast.LENGTH_SHORT)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}