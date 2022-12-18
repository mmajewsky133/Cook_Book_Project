package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.constants.MAX_LENGTH_STEP_DETAIL
import edu.uca.innovatech.cookbook.constants.MAX_TIME_PREP_INGREDIENT
import edu.uca.innovatech.cookbook.constants.MIN_LENGTH_STEP_DETAIL
import edu.uca.innovatech.cookbook.core.ex.loseFocusAfterAction
import edu.uca.innovatech.cookbook.core.ex.onTextChanged
import edu.uca.innovatech.cookbook.core.ex.showToast
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.databinding.FragmentEditStepDetailsBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel

class EditStepDetailsFragment : Fragment() {

    private lateinit var selectedImageUri: Uri
    private val navigationArgs: EditStepDetailsFragmentArgs by navArgs()
    lateinit var paso: Paso

    //Basicamente instancia el ViewModel
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
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
        initListeners()
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

    private fun initListeners() {
        binding.apply {
            tfTiempoPrepPaso.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tfTiempoPrepPaso.onTextChanged {
                tflTiempoPrepPaso.error =
                    if (tfTiempoPrepPaso.text.toString()
                            .isEmpty() || tfTiempoPrepPaso.text.toString().toInt() < 1
                    ) "Debe de ingresar un tiempo de preparacion para el paso"
                    else if (tfTiempoPrepPaso.text.toString()
                            .toInt() <= MAX_TIME_PREP_INGREDIENT
                    ) null
                    else "El tiempo de preparacion del paso es demasiado largo"
            }
            tfDetallePaso.onTextChanged {
                tflDetallePaso.error =
                    if (tfDetallePaso.text.toString().isEmpty() ||
                        tfDetallePaso.text.toString().length <= MIN_LENGTH_STEP_DETAIL
                    ) "El paso necesita detalles"
                    else if (tfDetallePaso.text.toString().length <= MAX_LENGTH_STEP_DETAIL
                    ) null
                    else "Detalle del paso muy grande"
            }
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

    //Pone datos generales a usar en el View
    private fun bind(paso: Paso) {
        binding.apply {
            topAppBar.title = "Paso ${paso.numPaso}"
            ivFotoPaso.setImageBitmap(paso.imagenPaso)
            tfTiempoPrepPaso.setText(paso.tiempo.toString())
            tfDetallePaso.setText(paso.detalle)

            //manejar el cambio de valores de los campos y accion en keyboard
            tfTiempoPrepPaso.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tfDetallePaso.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
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
            showToast("El paso se ha guardado", Toast.LENGTH_SHORT)
        } else {
            showToast("El paso no se ha guardado", Toast.LENGTH_SHORT)
        }
    }

    //Verifica si las entradas estan o no vacias, devuelve True si estan llenas y False si no
    private fun esValido(): Boolean {
        return with(binding) {
            tfTiempoPrepPaso.text.toString().isNotEmpty()
                    && tfTiempoPrepPaso.text.toString().toInt() <= 1
                    && tfTiempoPrepPaso.text.toString().toInt() <= MAX_TIME_PREP_INGREDIENT
                    && tfDetallePaso.text.toString().isNotEmpty()
                    && tfDetallePaso.text.toString().length >= MIN_LENGTH_STEP_DETAIL
                    && tfDetallePaso.text.toString().length <= MAX_LENGTH_STEP_DETAIL
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