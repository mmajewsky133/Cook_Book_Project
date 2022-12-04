package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.constants.MAX_CANT_INGREDIENT
import edu.uca.innovatech.cookbook.constants.MAX_KCAL_INGREDIENT
import edu.uca.innovatech.cookbook.constants.MAX_LENGTH_INGREDIENT_NAME
import edu.uca.innovatech.cookbook.core.ex.loseFocusAfterAction
import edu.uca.innovatech.cookbook.core.ex.onTextChanged
import edu.uca.innovatech.cookbook.core.ex.showMaterialDialog
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.databinding.FragmentEditIngredientsBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel

class EditIngredientsFragment : Fragment() {

    private val navigationArgs: EditIngredientsFragmentArgs by navArgs()
    lateinit var ingrediente: Ingrediente

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
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
        initListeners()
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

    private fun initListeners() {
        binding.apply {
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
            tfIngrediente.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tfIngrediente.onTextChanged {
                tflIngrediente.error =
                    if (tfIngrediente.text.toString().length <= MAX_LENGTH_INGREDIENT_NAME) null
                    else "El nombre del ingrediente es muy grande"
            }
            tfCantidadIngrediente.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tfCantidadIngrediente.onTextChanged {
                tflCantidadIngrediente.error =
                    if (tfCantidadIngrediente.text.toString().isEmpty())
                        "La cantidad del ingrediente no es valida"
                    else if (tfCantidadIngrediente.text.toString()
                            .toInt() <= MAX_CANT_INGREDIENT
                    ) null
                    else "La cantidad del ingrediente no es valida"
            }
            tfCalorias.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tfCalorias.onTextChanged {
                tflCalorias.error =
                    if (tfCalorias.text.toString().isEmpty())
                        "La cantidad de calorias no es valida"
                    else if (tfCalorias.text.toString()
                            .toInt() <= MAX_KCAL_INGREDIENT
                    ) null
                    else "La cantidad de calorias no es valida"
            }
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(ingrediente: Ingrediente) {
        binding.apply {
            tfIngrediente.setText(ingrediente.nombreIngrediente)
            tfCantidadIngrediente.setText(ingrediente.cantIngrediente.toString())
            tfMedidaIngrediente.setText(ingrediente.medidaIngrediente, false)
            tfCalorias.setText(ingrediente.caloriasIngrediente.toString())
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
                    && tfIngrediente.text.toString().length <= MAX_LENGTH_INGREDIENT_NAME
                    && tfCantidadIngrediente.text.toString().isNotEmpty()
                    && tfCantidadIngrediente.text.toString().toInt() <= MAX_CANT_INGREDIENT
                    && tfMedidaIngrediente.text.toString().isNotEmpty()
                    && tfCalorias.text.toString().isNotEmpty()
                    && tfCalorias.text.toString().toInt() <= MAX_KCAL_INGREDIENT
        }
    }


    private fun mostrarDialogConfirmacion() {
        showMaterialDialog(
            getString(android.R.string.dialog_alert_title),
            getString(R.string.delete_ingredient_dialog_msg),
            false,
            getString(R.string.no),
            getString(R.string.yes),
            {}, { eliminarIngrediente() }
        )
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}