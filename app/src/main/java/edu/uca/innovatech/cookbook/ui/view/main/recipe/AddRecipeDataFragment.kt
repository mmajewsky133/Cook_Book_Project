package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDataBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory
import edu.uca.innovatech.cookbook.core.ToAppFiles

class AddRecipeDataFragment : Fragment() {

    private lateinit var selectedImageUri: Uri

    //Basicamente comparte el ViewModel entre fragmentos
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

    private lateinit var binding: FragmentAddRecipeDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddRecipeDataBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            tfPasosReceta.addTextChangedListener(recipeTextWatcher)

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
                //Temp navigation
                agregarNuevaReceta()
                activity?.onBackPressed()
            }
        }
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfNombreReceta.text.toString().isNotEmpty()
                    && tfAutorReceta.text.toString().isNotEmpty()
                    && tfCategoriaReceta.text.toString().isNotEmpty()
                    && tfTiempoReceta.text.toString().isNotEmpty()
                    && tfTiempoReceta.text.toString().isNotEmpty()
                    && ivFotoReceta.drawable != null
        }
    }

    private fun agregarNuevaReceta() {

        if (esValido()) {
            viewModel.agregarReceta(
                selectedImageUri,
                binding.tfNombreReceta.text.toString(),
                binding.tfAutorReceta.text.toString(),
                binding.tfCategoriaReceta.text.toString(),
                binding.tfTiempoReceta.text.toString(),
                viewModel.pasosConverter(binding.tfPasosReceta.text.toString())
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
}