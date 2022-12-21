package com.example.apiarchetypereactive.model

data class Product(
    val id: String,
    val name: String,
    var skus: Set<SkuDTO>,
    var sellers: Set<SellerDTO>
)
