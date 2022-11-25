package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDataBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory
import kotlinx.coroutines.launch

class AddRecipeDataFragment : Fragment() {

    private val navigationArgs: AddRecipeDataFragmentArgs by navArgs()
    private lateinit var selectedImageUri: Uri
    lateinit var receta: RecetasConPasos

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModelFactory(
            (activity?.application as CookBookApp).database
                .RecetaDao()
        )
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

        with(binding) {
            btnSiguiente.isEnabled = false

            //Permitiendo que la flecha del TopAppBar funcione igual que darle hacia atras
            topAppBar.setNavigationOnClickListener() {
                activity?.onBackPressed()
            }

            //manejar el cambio de valores de los campos
            tfNombreReceta.addTextChangedListener(recipeTextWatcher)
            tfAutorReceta.addTextChangedListener(recipeTextWatcher)
            tfCategoriaReceta.addTextChangedListener(recipeTextWatcher)
            tfTiempoReceta.addTextChangedListener(recipeTextWatcher)

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
                    && tfAutorReceta.text.toString().isNotEmpty()
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

    //Objeto TextWatcher para manejar el cambio del TextField
    private val recipeTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        //Cuando Cambia el texto
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            // Habilita el boton dependiendo si los campos estan llenos o no
            binding.btnSiguiente.isEnabled = esValido()
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}