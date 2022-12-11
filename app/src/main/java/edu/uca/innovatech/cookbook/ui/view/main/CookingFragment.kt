package edu.uca.innovatech.cookbook.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.databinding.FragmentCookingBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.CookingOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.view.main.cooking.CookingActivity
import edu.uca.innovatech.cookbook.ui.view.main.recipe.SeeRecipeActivity
import edu.uca.innovatech.cookbook.ui.viewmodel.CookingViewModel

class CookingFragment : Fragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: CookingViewModel by activityViewModels {
        CookingViewModel.factory
    }

    // El ViewBinding
    private var _binding: FragmentCookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initRecyclerView()

        binding.fabNuevaReceta.setOnClickListener() {
            SelectRecipeBottomSheetFragment().show(childFragmentManager, "fromCooking")
        }
    }

    private fun initRecyclerView() {
        val adapter = CookingOverviewCardAdapter {
            //Codigo para cuando se presione en el CardView
            val intent = Intent(context, CookingActivity::class.java)
            intent.putExtra("id_cooking", it.cookingReceta.idCooking)
            intent.putExtra("current_step", it.cookingReceta.currentStep)

            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this.context)

        binding.rcvListaCocinar.layoutManager = layoutManager
        binding.rcvListaCocinar.adapter = adapter

        viewModel.allToCookingRecetas.observe(this.viewLifecycleOwner) { cocinando ->
            cocinando.let {
                adapter.submitList(it)
                binding.lNoCooking.isVisible = it.isEmpty()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}