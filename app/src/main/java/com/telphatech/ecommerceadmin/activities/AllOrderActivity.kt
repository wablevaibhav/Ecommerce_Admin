package com.telphatech.ecommerceadmin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.adapters.AllOrderAdapter
import com.telphatech.ecommerceadmin.databinding.ActivityAllOrderBinding
import com.telphatech.ecommerceadmin.models.AllOrder

class AllOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllOrderBinding
    private lateinit var list: ArrayList<AllOrder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        FirebaseFirestore.getInstance().collection("allOrders")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it) {
                    val data = doc.toObject(AllOrder::class.java)
                    list.add(data)
                }
                binding.recyclerView.adapter = AllOrderAdapter(list, this)
            }


    }
}