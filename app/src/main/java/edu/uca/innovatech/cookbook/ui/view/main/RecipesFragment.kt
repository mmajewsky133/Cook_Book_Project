package edu.uca.innovatech.cookbook.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.databinding.FragmentRecipesBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.view.main.recipe.AddRecipeActivity
import edu.uca.innovatech.cookbook.ui.view.main.recipe.SeeRecipeActivity
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class RecipesFragment : Fragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database.RecetaDao()
        )
    }

    //un lateinit var para las entidades a usar (en este caso solo la entidad receta)
    //lateinit var receta: Receta

    // El ViewBinding
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecipeOverviewCardAdapter {
            //Codigo para cuando se presione en el CardView
            val intent = Intent(context, SeeRecipeActivity::class.java)
            intent.putExtra("id_receta", it.id)

            startActivity(intent)
        }

        binding.rcvListaRecetas.layoutManager = LinearLayoutManager(this.context)
        binding.rcvListaRecetas.adapter = adapter

        binding.fabNuevaReceta.setOnClickListener() {
            val intent = Intent(context, AddRecipeActivity::class.java)
            context?.startActivity(intent)


        }

        viewModel.allRecetas.observe(this.viewLifecycleOwner) { recetas ->
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