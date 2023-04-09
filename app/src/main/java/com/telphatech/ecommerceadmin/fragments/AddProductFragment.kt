package com.telphatech.ecommerceadmin.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.telphatech.ecommerceadmin.adapters.AddProductImageAdapter
import com.telphatech.ecommerceadmin.models.AddProduct
import com.telphatech.ecommerceadmin.models.Category
import com.telphatech.ecommerceadmin.R
import com.telphatech.ecommerceadmin.databinding.FragmentAddProductBinding
import java.io.File
import java.util.*

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var adapter: AddProductImageAdapter
    private var coverImage: Uri? = null
    private lateinit var dialog: Dialog
    private var coverImageUrl: String? = ""
    private lateinit var categoryList: ArrayList<String>

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImage = it.data!!.data
            binding.imgProductCover.setImageURI(coverImage)
            binding.imgProductCover.visibility = VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private var launchProductActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageUrl = it.data!!.data
                list.add(imageUrl!!)
                adapter.notifyDataSetChanged()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(
            layoutInflater
        )
        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.imgSelectCover.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }
        binding.btnProduct.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }
        setProductCategory()
        adapter = AddProductImageAdapter(list)
        binding.RvProductImg.adapter = adapter
        binding.btnSubmitProduct.setOnClickListener { validateData() }
        return binding.root
    }

    private fun validateData() {
        if (Objects.requireNonNull(binding.EtProductName.text).toString().isEmpty()) {
            binding.EtProductName.requestFocus()
            binding.EtProductName.error = "Empty"
        } else if (Objects.requireNonNull(binding.EtProductDesc.text).toString().isEmpty()) {
            binding.EtProductDesc.requestFocus()
            binding.EtProductDesc.error = "Empty"
        } else if (Objects.requireNonNull(binding.EtProductSP.text).toString().isEmpty()) {
            binding.EtProductSP.requestFocus()
            binding.EtProductSP.error = "Empty"
        } else if (Objects.requireNonNull(binding.EtProductMRP.text).toString().isEmpty()) {
            binding.EtProductMRP.requestFocus()
            binding.EtProductMRP.error = "Empty"
        } else if (list.size < 1) {
            Toast.makeText(requireContext(), "Please select product images", Toast.LENGTH_SHORT)
                .show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()
        val file = File(UUID.randomUUID().toString() + ".jpg")
        val firebaseStorage = FirebaseStorage.getInstance().reference.child("products/$file")
        firebaseStorage.putFile(coverImage!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri: Uri ->
                    coverImageUrl = uri.toString()
                    uploadProductImage()
                }
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private var i = 0
    private fun uploadProductImage() {
        dialog.show()
        val file = File(UUID.randomUUID().toString() + ".jpg")
        val firebaseStorage = FirebaseStorage.getInstance().reference.child("products/$file")
        firebaseStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    if (list.size == listImages.size) {
                        storeData()
                    } else {
                        i += 1
                        uploadProductImage()
                    }
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = Firebase.firestore.collection("products")
        val key = db.document().id
        val data = AddProduct(
            binding.EtProductName.text.toString(),
           binding.EtProductDesc.text.toString(),
            coverImageUrl,
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.EtProductMRP.text.toString(),
            binding.EtProductSP.text.toString(),
            listImages
        )
        db.document(key).set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show()
                binding.EtProductName.text = null
                binding.EtProductDesc.text = null
                binding.EtProductMRP.text = null
                binding.EtProductSP.text = null
                binding.imgProductCover.setImageURI(null)
                binding.RvProductImg.adapter = null
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setProductCategory() {
        categoryList = ArrayList()
        Firebase.firestore.collection("categories").get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                categoryList.clear()
                for (snapshot in queryDocumentSnapshots) {
                    val data = snapshot.toObject(
                        Category::class.java
                    )
                    categoryList.add(data.category!!)
                }
                categoryList.add(0, "Select Category")
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryList)
                binding.productCategoryDropdown.adapter = arrayAdapter
            }
    }
}