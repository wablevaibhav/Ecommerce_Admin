package com.telphatech.ecommerceadmin.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.telphatech.ecommerceadmin.adapters.CategoryAdapter
import com.telphatech.ecommerceadmin.models.Category
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.databinding.FragmentCategoryBinding
import java.util.*

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private var imageUrl: Uri? = null
    private lateinit var dialog: Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater
        )
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        getData()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }

            btnUpload.setOnClickListener {
                validateData(binding.TLCategoryName.editText?.text.toString())
            }

        }


        return binding.root
    }

    private fun getData() {
        val list = ArrayList<Category?>()
        FirebaseFirestore.getInstance().collection("categories")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                list.clear()
                for (snapshot in queryDocumentSnapshots) {
                    val data = snapshot.toObject(Category::class.java)
                    list.add(data)
                }
                binding.RVCategory.adapter = CategoryAdapter(requireContext(), list)
            }
    }

    private fun validateData(categoryName: String) {
        if (categoryName.isEmpty()) {
            Toast.makeText(requireContext(), "Please provide category name", Toast.LENGTH_SHORT).show()
        } else if (imageUrl == null) {
            Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {

        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categoryName,image.toString())
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun storeData(categoryName: String, url: String) {

        val db = Firebase.firestore
        val data = hashMapOf<String, Any>(
            "category" to categoryName,
            "img" to url
        )

        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Category Updated", Toast.LENGTH_SHORT).show()
                binding.imageView.setImageDrawable(requireContext().resources.getDrawable(R.drawable.image))
                binding.TLCategoryName.editText?.text = null
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
}