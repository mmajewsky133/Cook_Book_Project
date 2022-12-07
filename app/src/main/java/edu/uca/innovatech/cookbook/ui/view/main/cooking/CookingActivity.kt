package edu.uca.innovatech.cookbook.ui.view.main.cooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.databinding.ActivityCookingBinding

class CookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCookingBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View Binding
        binding = ActivityCookingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_cooking) as NavHostFragment
        navController = navHostFragment.navController
    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}