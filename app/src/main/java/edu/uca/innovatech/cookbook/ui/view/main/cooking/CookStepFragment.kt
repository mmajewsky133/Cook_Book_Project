package edu.uca.innovatech.cookbook.ui.view.main.cooking

import android.os.Bundle
import android.os.CountDownTimer
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
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.RecetasConPasos
import edu.uca.innovatech.cookbook.databinding.FragmentCookStepBinding
import edu.uca.innovatech.cookbook.ui.viewmodel.CookingViewModel
import java.time.LocalTime

class CookStepFragment : Fragment() {

    private val navigationArgs: CookStepFragmentArgs by navArgs()
    private lateinit var timer: CountDownTimer
    private lateinit var cooking: CookingReceta
    private lateinit var receta: RecetasConPasos

    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: CookingViewModel by activityViewModels {
        CookingViewModel.factory
    }
    // El ViewBinding
    private var _binding: FragmentCookStepBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCookStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        //Conseguir id del navigation args
        val id = navigationArgs.idCooking
        val currentStep = navigationArgs.currentStep

        //Atraves del id consigue los datos del la receta a cocinar
        viewModel.agarrarToCookReceta(id).observe(this.viewLifecycleOwner) { datoCooking ->
            cooking = datoCooking.cookingReceta
            viewModel.agarrarReceta(cooking.idRecetaCooking).observe(this.viewLifecycleOwner) {
                receta = it
                bind(cooking, receta, currentStep)
                initListener(cooking, receta, currentStep)
            }
        }
    }

    private fun initListener(cooking: CookingReceta, receta: RecetasConPasos, currentStep: Int) {

        binding.btnSiguiente.setOnClickListener {
            actualizarPasoCooking(cooking, currentStep)

            //Debug
            println(receta.pasos.size)
            println(cooking.currentStep)

            if (receta.pasos.size > cooking.currentStep) {
                val action = CookStepFragmentDirections
                    .actionCookStepFragmentToPrepStepFragment(cooking.idCooking, currentStep+1)
                findNavController().navigate(action)
            } else {
                onFinishRecipe(cooking)
                val action = CookStepFragmentDirections
                    .actionCookStepFragmentToFinishedCookingFragment()
                findNavController().navigate(action)
            }
        }

        //debug
        binding.tvPbTiempoPrep.setOnClickListener{
            onFinishTimer()
        }
    }

    private fun initTimer(timeSec: Int){
        timer = object : CountDownTimer((timeSec * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val countingSeconds = millisUntilFinished / 1000
                onUpdateTimer(countingSeconds, timeSec)
            }
            override fun onFinish() {
                onFinishTimer()
            }
        }
    }

    //Pone datos generales a usar en el View
    private fun bind(cooking: CookingReceta, receta: RecetasConPasos, currentStep: Int) {
        binding.apply {

            topAppBar.title = receta.receta.nombre
            tvPreparacionOPasoTitleLabel.text = parseNumPaso(cooking.currentStep)

            initTimer(getCurrentStepTime(currentStep, receta) * 60)
            timer.start()

            if (currentStep == 0) {
                tvPreparacionLabel.text = getString(R.string.order_ingredients_label)
                tvPreparacionLabel.isVisible = true
            } else tvPreparacionLabel.isVisible = false

            tvIngredienteOPasoLable.text = parsePasoTODO(currentStep)
            tvIngredienteOPaso.text = getCurrentStepDetail(currentStep, receta)
            btnSiguiente.isEnabled = false
        }
    }

    private fun onUpdateTimer(countingSeconds: Long, timeTotal: Int) {
        binding.tvPbTiempoPrep.text = LocalTime.MIN.plusSeconds(countingSeconds).toString()
        binding.pbTiempoPrep.progress =
            ((countingSeconds.toDouble() / timeTotal.toDouble()) * 100).toInt()
    }

    private fun onFinishTimer(){
        timer.cancel()
        binding.btnSiguiente.isEnabled = true
    }

    private fun onCancelTimer() {
        timer.cancel()
    }

    private fun actualizarPasoCooking(cooking: CookingReceta, currentStep: Int) {
        viewModel.actualizarPasoCocinar(cooking, currentStep)
    }

    private fun onFinishRecipe(cooking: CookingReceta) {
        viewModel.terminarCocinar(cooking)
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

    override fun onDestroy() {
        super.onDestroy()
        onCancelTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}