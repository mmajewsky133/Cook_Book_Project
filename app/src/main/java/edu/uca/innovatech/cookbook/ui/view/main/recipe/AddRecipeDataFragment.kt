package edu.uca.innovatech.cookbook.ui.view.main.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.databinding.FragmentAddRecipeDataBinding
import edu.uca.innovatech.cookbook.databinding.FragmentRecipesBinding

class AddRecipeDataFragment : Fragment() {

    private lateinit var binding: FragmentAddRecipeDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddRecipeDataBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

}