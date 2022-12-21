package com.example.apiarchetypereactive.model

import com.example.apiarchetypereactive.model.dto.SellerDTO
import com.example.apiarchetypereactive.model.dto.SkuDTO

data class Product(
    val id: String,
    val name: String,
    var skus: Set<SkuDTO>,
    var sellers: Set<SellerDTO>
)
