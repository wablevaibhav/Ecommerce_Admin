package com.telphatech.ecommerceadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telphatech.ecommerceadmin.adapters.CategoryAdapter.CategoryAdapterViewHolder
import com.telphatech.ecommerceadmin.models.Category
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.databinding.ItemCategoryLayoutBinding

class CategoryAdapter(private val context: Context, private val category: ArrayList<Category?>) :
    RecyclerView.Adapter<CategoryAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapterViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_category_layout, parent, false)
        return CategoryAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapterViewHolder, position: Int) {
        val categories = category[position]
        holder.binding.textView2.text = categories?.category
        Glide.with(context).load(categories?.img).into(holder.binding.imageView2)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    class CategoryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemCategoryLayoutBinding

        init {
            binding = ItemCategoryLayoutBinding.bind(itemView)
        }
    }
}