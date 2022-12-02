package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.core.ex.loseFocusAfterAction
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentEditIngredientDetailsBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.IngredientsDetailsCardAdapter
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class EditIngredientDetailsFragment : Fragment() {

    private val navigationArgs: EditIngredientDetailsFragmentArgs by navArgs()
    lateinit var receta: RecetasConPasos
    lateinit var ingredientes: List<Ingrediente>

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database
                .RecetaDao()
        )
    }

    private var _binding: FragmentEditIngredientDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditIngredientDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        //Conseguir id del navigation args
        val id = navigationArgs.idReceta

        //Atraves del id se obtiene los datos generales de la receta recien guardada
        // mas los pasos (vacios)
        viewModel.agarrarReceta(id).observe(this.viewLifecycleOwner) { selectedItem ->
            receta = selectedItem
            bind(receta)
        }

        val adapter = IngredientsDetailsCardAdapter {
            guardarEdiciones()
            val action = EditIngredientDetailsFragmentDirections
                .actionEditIngredientDetailsFragmentToEditIngredientsFragment(id, it.idIngrediente)
            findNavController().navigate(action)
        }

        binding.rcvIngredientes.layoutManager = LinearLayoutManager(this.context)
        binding.rcvIngredientes.adapter = adapter

        viewModel.agarrarIngredientes(id).observe(this.viewLifecycleOwner) { selectedIngredientes ->
            selectedIngredientes.let {
                ingredientes = it
                adapter.submitList(ingredientes)
            }
        }

        binding.btnAddIngredients.setOnClickListener{
            agregarIngrediente()
        }
    }

    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            tfTiempoPrepPrep.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tfTiempoPrepPrep.setText(receta.receta.tiempoPrepPrep.toString())

            topAppBar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.guardar -> {
                        guardarEdiciones()
                        activity?.onBackPressedDispatcher?.onBackPressed()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun guardarEdiciones() {
        if (esValido()) {
            viewModel.guardarCambiosTiempoPrepPrep(
                receta,
                binding.tfTiempoPrepPrep.text.toString().toInt()
            )
        } else {
            //dialog sayings shits broken, do it again
        }
    }

    private fun agregarIngrediente() {
        viewModel.agregarNuevoIngrediente(receta.receta.id)
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfTiempoPrepPrep.text.toString().isNotEmpty()
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