package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.net.Uri
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
import edu.uca.innovatech.cookbook.CookBookApp
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.databinding.FragmentEditStepDetailsBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModelFactory

class EditStepDetailsFragment : Fragment() {

    private lateinit var selectedImageUri: Uri
    private val navigationArgs: EditStepDetailsFragmentArgs by navArgs()
    lateinit var paso: Paso

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
            binding.ivFotoPaso.setImageURI(uri)
        }
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

        binding.ivFotoPasoHolder.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(paso: Paso) {
        binding.apply {
            topAppBar.title = "Paso ${paso.numPaso}"
            ivFotoPaso.setImageBitmap(paso.imagenPaso)
            tfTiempoPrepPaso.setText(paso.tiempo.toString())
            tfDetallePaso.setText(paso.detalle)

            topAppBar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.guardar -> {
                        guardarEdicionesPaso()
                        activity?.onBackPressedDispatcher?.onBackPressed()
                        true
                    }
                    else -> false
                }
            }
        }

    }

    private fun guardarEdicionesPaso() {
        if (esValido()) {
            viewModel.guardarCambiosPaso(
                navigationArgs.idPaso,
                navigationArgs.idReceta,
                paso.numPaso,
                binding.ivFotoPaso.drawable.toBitmap(),
                binding.tfTiempoPrepPaso.text.toString().toInt(),
                binding.tfDetallePaso.text.toString()
            )
        } else {
            //dialog sayings shits broken, do it again
        }
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfTiempoPrepPaso.text.toString().isNotEmpty()
                    && tfDetallePaso.text.toString().isNotEmpty()
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