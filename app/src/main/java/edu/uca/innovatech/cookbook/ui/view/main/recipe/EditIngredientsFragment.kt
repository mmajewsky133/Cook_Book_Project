package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.databinding.FragmentEditIngredientsBinding
import edu.uca.innovatech.cookbook.databinding.FragmentEditStepDetailsBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class EditIngredientsFragment : Fragment() {

    private val navigationArgs: EditIngredientsFragmentArgs by navArgs()
    lateinit var ingrediente: Ingrediente

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database
                .RecetaDao()
        )
    }

    private var _binding: FragmentEditIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        //Conseguir id del navigation args
        val id = navigationArgs.idIngrediente
        val idReceta = navigationArgs.idReceta

        //Atraves del id y el id de su receta se obtiene los datos del paso
        viewModel.agarrarIngrediente(id, idReceta)
            .observe(this.viewLifecycleOwner) { selectedItem ->
                ingrediente = selectedItem
                bind(ingrediente)
            }

    }

    //Pone datos generales a usar en el View
    private fun bind(ingrediente: Ingrediente) {
        binding.apply {
            tfIngrediente.setText(ingrediente.nombreIngrediente)
            tfCantidadIngrediente.setText(ingrediente.cantIngrediente.toString())
            tfCalorias.setText(ingrediente.caloriasIngrediente.toString())

            topAppBar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.eliminar -> {
                        mostrarDialogConfirmacion()
                        true
                    }
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
            viewModel.guardarCambiosIngrediente(
                navigationArgs.idIngrediente,
                navigationArgs.idReceta,
                binding.tfIngrediente.text.toString(),
                binding.tfCantidadIngrediente.text.toString().toInt(),
                binding.tfMedidaIngrediente.text.toString(),
                binding.tfCalorias.text.toString().toInt()
            )
        } else {
            //dialog sayings shits broken, do it again
        }
    }

    private fun eliminarIngrediente() {
        viewModel.eliminarIngrediente(
            navigationArgs.idIngrediente,
            navigationArgs.idReceta
        )
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfIngrediente.text.toString().isNotEmpty()
                    && tfCantidadIngrediente.text.toString().isNotEmpty()
                    && tfMedidaIngrediente.text.toString().isNotEmpty()
                    && tfCalorias.text.toString().isNotEmpty()
        }
    }

    private fun mostrarDialogConfirmacion() {
        this.context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(android.R.string.dialog_alert_title))
                .setMessage(getString(R.string.delete_ingredient_dialog_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    eliminarIngrediente()
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