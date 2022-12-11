package edu.uca.innovatech.cookbook.ui.view.main.cooking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.core.util.parseIngredientes
import edu.uca.innovatech.cookbook.core.util.parseNumPaso
import edu.uca.innovatech.cookbook.core.util.parsePasoTODO
import edu.uca.innovatech.cookbook.core.util.parseTiempoPrep
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentCookStepBinding
import edu.uca.innovatech.cookbook.databinding.FragmentPrepStepBinding
import edu.uca.innovatech.cookbook.ui.view.main.recipe.AddRecipeDataFragmentDirections
import edu.uca.innovatech.cookbook.ui.viewmodel.CookingViewModel

class PrepStepFragment : Fragment() {

    private val navigationArgs: PrepStepFragmentArgs by navArgs()
    lateinit var cooking: CookingReceta
    lateinit var receta: RecetasConPasos

    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: CookingViewModel by activityViewModels {
        CookingViewModel.factory
    }

    // El ViewBinding
    private var _binding: FragmentPrepStepBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPrepStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val id: Int
        val currentStep: Int

        //Conseguir IDs
        if (navigationArgs.idCooking == -1) {
            id = activity?.intent?.getIntExtra("id_cooking", 0)!!
            currentStep = activity?.intent?.getIntExtra("current_step", 0)!!
        } else {
            id = navigationArgs.idCooking
            currentStep = navigationArgs.currentStep
        }

        //Atraves del id consigue los datos del la receta a cocinar
        viewModel.agarrarToCookReceta(id).observe(this.viewLifecycleOwner) { datoCooking ->
            cooking = datoCooking.cookingReceta
            viewModel.agarrarReceta(datoCooking.cookingReceta.idRecetaCooking)
                .observe(this.viewLifecycleOwner) {
                    receta = it
                    bind(cooking, receta, currentStep)
                    initListener(cooking)
                }
        }
    }

    private fun initListener(cooking: CookingReceta){
        binding.btnSiguiente.setOnClickListener {
            val action = PrepStepFragmentDirections
                .actionPrepStepFragmentToCookStepFragment(
                    cooking.idCooking,
                    cooking.currentStep
                )
            findNavController().navigate(action)
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(cooking: CookingReceta, receta: RecetasConPasos, currentStep: Int) {
        binding.apply {
            topAppBar.title = receta.receta.nombre
            tvPreparacionOPasoTitleLabel.text = parseNumPaso(cooking.currentStep)
            tvTiempoPreparacion.text = parseTiempoPrep(getCurrentStepTime(currentStep, receta))
            tvIngredienteLable.isVisible = currentStep == 0
            tvIngredienteOrDetallesPaso.text = getCurrentStepDetail(currentStep, receta)
        }
    }

    private fun getCurrentStepTime(currentStep: Int, receta: RecetasConPasos): Int {
        if (currentStep == 0) return receta.receta.tiempoPrepPrep
        for (paso in receta.pasos) {
            if (paso.numPaso == currentStep)
                return paso.tiempo
        }
        return 0
    }

    private fun getCurrentStepDetail(currentStep: Int, receta: RecetasConPasos): String {
        if (currentStep == 0) return parseIngredientes(receta.ingredientes)
        for (paso in receta.pasos) {
            if (paso.numPaso == currentStep)
                return paso.detalle
        }
        return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}