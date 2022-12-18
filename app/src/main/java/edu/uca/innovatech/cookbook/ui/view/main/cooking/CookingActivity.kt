package edu.uca.innovatech.cookbook.ui.view.main.cooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.core.ex.showMaterialDialog
import edu.uca.innovatech.cookbook.databinding.ActivityCookingBinding

class CookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCookingBinding
    private lateinit var navController: NavController

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showMaterialDialog(
                "",
                "",
                true,
                "No", "Yes", {}, {finish()}
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //onBackPressed
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

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