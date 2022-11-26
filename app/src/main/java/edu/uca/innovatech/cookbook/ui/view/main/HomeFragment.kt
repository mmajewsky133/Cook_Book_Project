package edu.uca.innovatech.cookbook.ui.view.main

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.databinding.FragmentHomeBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeSelectionCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory
import java.util.*


class HomeFragment : Fragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database.RecetaDao()
        )
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

        if (currentHour in 4..12) {
            binding.tvTiempoDia.text = "Buenos Dias,"
        } else if (currentHour in 13..18) {
            binding.tvTiempoDia.text = "Buenas Tardes,"
        } else {
            binding.tvTiempoDia.text = "Buenas Noches,"
        }

        val adapter = RecipeSelectionCardAdapter {
            //Codigo para cuando se presione en el boton CardView
            mostrarDialogConfirmacion()
        }
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        binding.rcvListaRecetas.layoutManager = layoutManager
        binding.rcvListaRecetas.adapter = adapter

        if (currentHour in 5..10) {
            viewModel.allRecetasTiempo("Desayuno").observe(this.viewLifecycleOwner) { recetas ->
                recetas.let {
                    adapter.submitList(it)
                }
            }
        } else if (currentHour in 11..16) {
            viewModel.allRecetasTiempo("Almuerzo").observe(this.viewLifecycleOwner) { recetas ->
                recetas.let {
                    adapter.submitList(it)
                }
            }
        } else if (currentHour in 18..21) {
            viewModel.allRecetasTiempo("Cena").observe(this.viewLifecycleOwner) { recetas ->
                recetas.let {
                    adapter.submitList(it)
                }
            }
        } else {
            viewModel.allRecetasTiempo("Snack").observe(this.viewLifecycleOwner) { recetas ->
                recetas.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun mostrarDialogConfirmacion() {
        this.context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.start_cooking))
                .setMessage(getString(R.string.cook_recipe_dialog_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    //cocinarReceta()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}