package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDetailBinding
import edu.uca.innovatech.cookbook.databinding.FragmentEditStepDetailsBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class EditStepDetailsFragment : Fragment() {

    private val navigationArgs: EditStepDetailsFragmentArgs by navArgs()
    lateinit var paso: Paso

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database
                .RecetaDao()
        )
    }

    private var _binding: FragmentEditStepDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        _binding = FragmentEditStepDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        //Conseguir id del navigation args
        val id = navigationArgs.idPaso
        val idReceta = navigationArgs.idReceta

        //Atraves del id y el id de su receta se obtiene los datos del paso
        viewModel.agarrarPaso(id, idReceta).observe(this.viewLifecycleOwner) { selectedItem ->
            paso = selectedItem
            bind(paso)
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(paso: Paso) {
        binding.apply {
            topAppBar.title = "Paso ${paso.numPaso}"
            ivFotoPaso.setImageBitmap(paso.imagenPaso)
            if (paso.tiempo != 0)
                tfTiempoPrepPaso.setText(paso.tiempo)
            tfDetallePaso.setText(paso.detalle)

            topAppBar.setNavigationOnClickListener { //Goback or Save }
            }
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