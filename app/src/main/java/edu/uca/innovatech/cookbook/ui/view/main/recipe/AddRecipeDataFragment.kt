package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.constants.MAX_LENGTH_TITLE_AUTHOR
import edu.uca.innovatech.cookbook.core.ex.loseFocusAfterAction
import edu.uca.innovatech.cookbook.core.ex.onTextChanged
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDataBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import kotlinx.coroutines.launch

class AddRecipeDataFragment : Fragment() {

    private val navigationArgs: AddRecipeDataFragmentArgs by navArgs()
    private lateinit var selectedImageUri: Uri
    lateinit var receta: RecetasConPasos

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
    }

    //para el Media Picker
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivFotoReceta.setImageURI(uri)
            binding.btnSiguiente.isEnabled = esValido()
        } else {
            binding.btnSiguiente.isEnabled = esValido()
        }
    }

    private var _binding: FragmentAddRecipeDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddRecipeDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        //Conseguir id del navigation args en caso de editar
        val idRecetaFrom = navigationArgs.idReceta

        if (idRecetaFrom != -1) {
            viewModel.agarrarReceta(idRecetaFrom).observe(this.viewLifecycleOwner) { selectedItem ->
                receta = selectedItem
                bind(receta)
            }
        }

        initListeners(idRecetaFrom)
    }

    //Inicializa listeners de botones y cambios
    private fun initListeners(idRecetaFrom: Int) {
        with(binding) {
            btnSiguiente.isEnabled = false

            //Permitiendo que la flecha del TopAppBar funcione igual que darle hacia atras
            topAppBar.setNavigationOnClickListener() {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            //manejar el cambio de valores de los campos y accion en keyboard
            tfNombreReceta.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tfNombreReceta.onTextChanged {
                btnSiguiente.isEnabled = esValido()
                tflNombreReceta.error =
                    if (tfNombreReceta.text.toString().length <= MAX_LENGTH_TITLE_AUTHOR) null
                    else "Nombre de la receta muy grande"
            }
            tfAutorReceta.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tfAutorReceta.onTextChanged {
                btnSiguiente.isEnabled = esValido()
                tflAutorReceta.error =
                    if (tfAutorReceta.text.toString().length <= MAX_LENGTH_TITLE_AUTHOR) null
                    else "Nombre del autor muy grande"
            }

            tfCategoriaReceta.onTextChanged { binding.btnSiguiente.isEnabled = esValido() }
            tfTiempoReceta.onTextChanged { binding.btnSiguiente.isEnabled = esValido() }

            //un click listener para escoger una image para la receta
            ivFotoRecetaHolder.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            //un click listener para el boton siguiente
            btnSiguiente.setOnClickListener() {
                if (idRecetaFrom != -1) {
                    actualizarReceta()

                    val action = AddRecipeDataFragmentDirections
                        .actionAddRecipeDataFragmentToAddRecipeDetailFragment(idRecetaFrom)
                    findNavController().navigate(action)

                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val id = agregarNuevaReceta()

                        val action = AddRecipeDataFragmentDirections
                            .actionAddRecipeDataFragmentToAddRecipeDetailFragment(id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(receta: RecetasConPasos) {
        binding.apply {
            topAppBar.title = receta.receta.nombre
            ivFotoReceta.setImageBitmap(receta.receta.bitmapImagen)
            tfNombreReceta.setText(receta.receta.nombre)
            tfAutorReceta.setText(receta.receta.autor)

            tfCategoriaReceta.setText(receta.receta.categoria, false)
            tfTiempoReceta.setText(receta.receta.tiempo, false)
        }
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfNombreReceta.text.toString().isNotEmpty()
                    && tfNombreReceta.text.toString().length <= MAX_LENGTH_TITLE_AUTHOR
                    && tfAutorReceta.text.toString().isNotEmpty()
                    && tfAutorReceta.text.toString().length <= MAX_LENGTH_TITLE_AUTHOR
                    && tfCategoriaReceta.text.toString().isNotEmpty()
                    && tfTiempoReceta.text.toString().isNotEmpty()
        }
    }

    private suspend fun agregarNuevaReceta(): Int {
        if (esValido()) {
            return viewModel.agregarReceta(
                binding.ivFotoReceta.drawable.toBitmap(),
                binding.tfNombreReceta.text.toString(),
                binding.tfAutorReceta.text.toString(),
                binding.tfCategoriaReceta.text.toString(),
                binding.tfTiempoReceta.text.toString()
            )
        }
        return 0
    }

    private fun actualizarReceta() {
        if (esValido()) {
            viewModel.actualizarReceta(
                receta.receta.id,
                binding.ivFotoReceta.drawable.toBitmap(),
                binding.tfNombreReceta.text.toString(),
                binding.tfAutorReceta.text.toString(),
                binding.tfCategoriaReceta.text.toString(),
                binding.tfTiempoReceta.text.toString()
            )
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