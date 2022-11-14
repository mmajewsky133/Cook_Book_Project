package edu.uca.innovatech.cookbook.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.uca.innovatech.cookbook.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.moreConfig.setOnClickListener(){

        }
    }
}