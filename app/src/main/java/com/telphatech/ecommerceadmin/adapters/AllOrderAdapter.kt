package com.telphatech.ecommerceadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.telphatech.ecommerceadmin.databinding.ItemAllOrderBinding
import com.telphatech.ecommerceadmin.models.AllOrder

class AllOrderAdapter(val list : ArrayList<AllOrder>, val context: Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>()
{

    inner class AllOrderViewHolder(val binding : ItemAllOrderBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(ItemAllOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.txtName.text = list[position].name
        holder.binding.txtPrice.text = list[position].price

        holder.binding.btnCancel.setOnClickListener {
//            holder.binding.btnProceed.text = "Cancelled"
            holder.binding.btnProceed.visibility = GONE
            updateStatus("Cancelled",list[position].orderId!!)
        }

        when(list[position].status) {
            "Ordered" -> {
                holder.binding.btnProceed.text = "Dispatched"
                holder.binding.btnProceed.setOnClickListener {
                    updateStatus("Dispatched",list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.btnProceed.text = "Delivered"
                holder.binding.btnProceed.setOnClickListener {
                    updateStatus("Delivered",list[position].orderId!!)
                }
            }

            "Delivered" -> {
                holder.binding.btnCancel.visibility = GONE
                holder.binding.btnProceed.isEnabled = false
                holder.binding.btnProceed.text = " Already Delivered"
            }
            "Cancelled" -> {
                holder.binding.btnProceed.visibility = GONE
                holder.binding.btnCancel.isEnabled = false
            }
        }

    }

    fun updateStatus(str : String, doc : String) {

        val data = hashMapOf<String, Any>()
        data["status"] = str

        FirebaseFirestore.getInstance().collection("allOrders").document(doc)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(context,"Status Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
    }


}