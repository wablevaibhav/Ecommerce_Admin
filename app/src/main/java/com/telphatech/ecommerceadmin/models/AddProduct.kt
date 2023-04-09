package com.telphatech.ecommerceadmin.models

class AddProduct(
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId: String? = "",
    val productMRP: String? = "",
    val productSP: String? = "",
    val productImages: ArrayList<String>
)