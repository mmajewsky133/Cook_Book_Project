package edu.uca.innovatech.cookbook.ui.view.main.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uca.innovatech.cookbook.R
import edu.uca.innovatech.cookbook.databinding.ActivityAddRecipeBinding

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRecipeBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}