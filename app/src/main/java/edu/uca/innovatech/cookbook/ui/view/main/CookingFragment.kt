package edu.uca.innovatech.cookbook.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.databinding.FragmentCookingBinding
import edu.uca.innovatech.cookbook.databinding.FragmentRecipesBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.view.main.recipe.AddRecipeActivity
import edu.uca.innovatech.cookbook.ui.view.main.recipe.SeeRecipeActivity

class CookingFragment : Fragment() {

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
            SelectRecipeBottomSheetFragment().show(childFragmentManager, "fromHome")
        }
    }

    private fun initRecyclerView() {
        //TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}