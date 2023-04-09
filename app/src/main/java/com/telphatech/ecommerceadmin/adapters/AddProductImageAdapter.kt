package com.telphatech.ecommerceadmin.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telphatech.ecommerceadmin.adapters.AddProductImageAdapter.AddProductImageAdapterViewHolder
import com.telphatech.ecommerceadmin.databinding.ImageItemBinding

class AddProductImageAdapter(
    private val addProducts: ArrayList<Uri>
) : RecyclerView.Adapter<AddProductImageAdapterViewHolder>() {


    inner class AddProductImageAdapterViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddProductImageAdapterViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddProductImageAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddProductImageAdapterViewHolder, position: Int) {
        holder.binding.imgItem.setImageURI(addProducts[position])
    }

    override fun getItemCount(): Int {
        return addProducts.size
    }


}