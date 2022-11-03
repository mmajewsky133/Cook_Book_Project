package edu.uca.innovatech.cookbook

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import edu.uca.innovatech.cookbook.databinding.ActivityMainBinding
import edu.uca.innovatech.cookbook.ui.main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Basic Binding que bindea el activity main con el layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hace que el active selector sea el color correcto
        //binding.bottomNavigation.itemActiveIndicatorColor =
            //getColorStateList(R.color.SelectedItemGreen)

        //Selecciona el item correcto en el bottomnav (home) porque por defecto pone el primero
        replaceFragment(HomeFragment())
        binding.bottomNavigation.selectedItemId = R.id.page_Home

        // basicamente maneja el a que pagina se va si le das click
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {

                R.id.page_health -> {
                    replaceFragment(HealthFragment())
                    true
                }
                R.id.page_Recipes -> {
                    replaceFragment(RecipesFragment())
                    true
                }
                R.id.page_Home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.page_Cooking -> {
                    replaceFragment(CookingFragment())
                    true
                }
                R.id.page_More -> {
                    replaceFragment(MoreFragment())
                    true
                }

                else -> false
            }
        }
    }


    private fun replaceFragment(fragment : Fragment){
        println("Enters the replace Fragment")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        println("does Commit?")
    }
}