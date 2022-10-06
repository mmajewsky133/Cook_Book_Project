package edu.uca.innovatech.cookbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_health -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.page_Recipes -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.page_Home -> {
                    // Respond to navigation item 3 click
                    true
                }
                R.id.page_Cooking -> {
                    // Respond to navigation item 4 click
                    true
                }
                R.id.page_More -> {
                    // Respond to navigation item 5 click
                    true
                }
                else -> false
            }
        }
    }
}