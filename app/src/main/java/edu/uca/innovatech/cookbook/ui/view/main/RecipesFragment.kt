package edu.uca.innovatech.cookbook.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uca.innovatech.cookbook.databinding.FragmentRecipesBinding
import edu.uca.innovatech.cookbook.ui.view.main.recipe.AddRecipeActivity

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipesBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initRecyclerViewRecetas()

        binding.fabNuevaReceta.setOnClickListener(){
            val intent = Intent(context, AddRecipeActivity::class.java)
            context?.startActivity(intent)
        }
    }

    /*
    private fun initRecyclerViewRecetas(){
        with (binding){
            rcvListaRecetas.layoutManager = LinearLayoutManager(context)
            rcvListaRecetas.adapter = RecipeOverviewCardAdapter()
        }

    }
    */
}