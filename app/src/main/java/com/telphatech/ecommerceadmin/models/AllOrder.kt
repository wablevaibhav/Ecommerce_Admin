package com.telphatech.ecommerceadmin.models

data class AllOrder(
    val name : String? = "",
    val orderId : String? = "",
    val userId : String? = "",
    val status : String? = "",
    val productId : String? = "",
    val price : String? = "",
)