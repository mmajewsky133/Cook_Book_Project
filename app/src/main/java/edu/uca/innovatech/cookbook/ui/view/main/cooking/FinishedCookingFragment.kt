package edu.uca.innovatech.cookbook.ui.view.main.cooking

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import edu.uca.innovatech.cookbook.core.ex.showMaterialDialog
import edu.uca.innovatech.cookbook.databinding.FragmentFinishedCookingBinding
import kotlinx.coroutines.*
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class FinishedCookingFragment : Fragment() {

    //onBackPressed Override
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    // El ViewBinding
    private var _binding: FragmentFinishedCookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFinishedCookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onBackPressed
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        startConfetti()
        initAnims()

        binding.btnTerminar.setOnClickListener{

            activity?.finish()
        }
    }

    private fun startConfetti(){
        val bundleColors = getThemeColors()
        val colorsList = listOf(
            bundleColors.getInt("primary"),
            bundleColors.getInt("secondary"),
            bundleColors.getInt("accent")
        )

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            angle = 310,
            spread = 50,
            colors = colorsList,
            timeToLive = 1500,
            position = Position.Relative(0.0, 0.6),
            emitter = Emitter(duration = 1, TimeUnit.SECONDS).perSecond(30)
        )
        binding.konfettiView.start(
            party,
            party.copy(
                angle = 230,
                position = Position.Relative(1.0, 0.6)
            )
        )
    }

    private fun initAnims() {
       MainScope().launch {
            delay(1000)
            ObjectAnimator.ofFloat(binding.tvTerminado, "translationY", -800f).apply {
                duration = 1000
                interpolator = FastOutSlowInInterpolator()
            }.start()
        }
    }

    private fun getThemeColors(): Bundle {
        val bundle = Bundle()
        val typedValue = TypedValue()
        if (context?.theme?.resolveAttribute((android.R.attr.colorPrimary), typedValue, true) == true){
            bundle.putInt("primary", typedValue.data)
        }
        if (context?.theme?.resolveAttribute((android.R.attr.colorSecondary), typedValue, true) == true){
            bundle.putInt("secondary", typedValue.data)
        }
        if (context?.theme?.resolveAttribute((android.R.attr.colorAccent), typedValue, true) == true){
            bundle.putInt("accent", typedValue.data)
        }
        return bundle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}