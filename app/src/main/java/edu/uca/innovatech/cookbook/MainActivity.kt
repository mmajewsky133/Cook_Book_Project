package edu.uca.innovatech.cookbook

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import edu.uca.innovatech.cookbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Basic Binding que bindea el activity main con el layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se asigna el navHostFragment y luego se saca su navController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //hace que el active selector sea el color correcto
        binding.bottomNavigation.itemActiveIndicatorColor =
            getColorStateList(R.color.SelectedItemGreen)

        //basicamente maneja el a que pagina se va si le das click
        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_health -> {
                    //Manda a la pagina health
                    true
                }
                R.id.page_Recipes -> {
                    //Manda a la pagina recipes
                    true
                }
                R.id.page_Home -> {
                    //Manda a la pagina Home
                    true
                }
                R.id.page_Cooking -> {
                    //Manda a la pagina cooking
                    true
                }
                R.id.page_More -> {
                    //Manda a la pagina more
                    true
                }
                else -> false
            }
        }
    }
}