package com.telphatech.ecommerceadmin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.telphatech.ecommerceadmin.activities.AllOrderActivity
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.btnAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }

        binding.btnAddSlider.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }

        binding.btnOrders.setOnClickListener {
            startActivity(Intent(requireContext(), AllOrderActivity::class.java))
        }

        return binding.root
    }
}