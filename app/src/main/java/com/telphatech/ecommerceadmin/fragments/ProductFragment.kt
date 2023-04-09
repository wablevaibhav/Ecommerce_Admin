package com.telphatech.ecommerceadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(
            layoutInflater
        )
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
        }
        return binding.root
    }
}