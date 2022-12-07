package edu.uca.innovatech.cookbook.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uca.innovatech.cookbook.databinding.FragmentSelectRecipeBottomSheetBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.CookingViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel

class SelectRecipeBottomSheetFragment : BottomSheetDialogFragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModelRecipes: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
    }
    private val viewModelCooking: CookingViewModel by activityViewModels {
        CookingViewModel.factory
    }

    //el view binding
    private var _binding: FragmentSelectRecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectRecipeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val adapter = RecipeOverviewCardAdapter {
            //Codigo para cuando se presione en el CardView
            //Mandar a Crear un Nuevo Cooking
            viewModelCooking.agregarCocinar(it.id)
            // Cierra el BottomSheetFragment
            dismiss()
        }
        val layoutManager = LinearLayoutManager(this.context)

        binding.rcvListaRecetas.layoutManager = layoutManager
        binding.rcvListaRecetas.adapter = adapter

        viewModelRecipes.allRecetas.observe(this.viewLifecycleOwner) { recetas ->
            recetas.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}