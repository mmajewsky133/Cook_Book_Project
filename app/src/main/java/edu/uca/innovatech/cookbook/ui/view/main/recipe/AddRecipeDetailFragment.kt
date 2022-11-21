package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDetailBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class AddRecipeDetailFragment : Fragment() {

    private val navigationArgs: AddRecipeDataFragmentArgs by navArgs()
    lateinit var receta: RecetasConPasos

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
        //Conseguir id del navigation args
        val id = navigationArgs.recetaId

        //Atraves del id se obtiene los datos generales de la receta recien guardada
        // mas los pasos (vacios)
        viewModel.agarrarReceta(id).observe(this.viewLifecycleOwner) { selectedItem ->
            receta = selectedItem
            bind(receta)
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(receta: RecetasConPasos) {

    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}