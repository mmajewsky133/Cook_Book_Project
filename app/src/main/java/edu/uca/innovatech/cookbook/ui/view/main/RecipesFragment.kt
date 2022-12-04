package edu.uca.innovatech.cookbook.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.databinding.FragmentRecipesBinding
import edu.uca.innovatech.cookbook.ui.view.adapter.RecipeOverviewCardAdapter
import edu.uca.innovatech.cookbook.ui.view.main.recipe.AddRecipeActivity
import edu.uca.innovatech.cookbook.ui.view.main.recipe.SeeRecipeActivity
import edu.uca.innovatech.cookbook.ui.viewmodel.RecipesViewModel

class RecipesFragment : Fragment() {
    //Basicamente comparte el ViewModel entre fragmentos
    private val viewModel: RecipesViewModel by activityViewModels {
        RecipesViewModel.factory
    }

    //un lateinit var para las entidades a usar (en este caso solo la entidad receta)
    //lateinit var receta: Receta

    // El ViewBinding
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initRecyclerView()

        binding.fabNuevaReceta.setOnClickListener() {
            val intent = Intent(context, AddRecipeActivity::class.java)
            context?.startActivity(intent)
        }

        //Detecta Scrolling del Recycler View
        binding.rcvListaRecetas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //Detecta la direccion en la cual se mueve el Recycler View
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Si hay movimiento hacia abajo, el FAB se encoje
                if (dy > 0) {
                    binding.fabNuevaReceta.shrink()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //Si no se puede scroll de manera vertical y el estado del Recycler View esta Idle
                //Indica que el Recycler View ha llegado al tope de arriba
                if (!recyclerView.canScrollVertically(-1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Vuelve a extender el FAB
                    binding.fabNuevaReceta.extend()
                }
            }
        })
    }

    private fun initRecyclerView() {
        val adapter = RecipeOverviewCardAdapter {
            //Codigo para cuando se presione en el CardView
            val intent = Intent(context, SeeRecipeActivity::class.java)
            intent.putExtra("id_receta", it.id)

            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this.context)

        binding.rcvListaRecetas.layoutManager = layoutManager
        binding.rcvListaRecetas.adapter = adapter

        viewModel.allRecetas.observe(this.viewLifecycleOwner) { recetas ->
            recetas.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}