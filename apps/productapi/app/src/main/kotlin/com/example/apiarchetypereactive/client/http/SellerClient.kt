package com.example.apiarchetypereactive.client.http

import com.example.apiarchetypereactive.model.SellerDTO
import com.example.apiarchetypereactive.model.SkuDTO
import feign.Param
import feign.RequestLine
import org.springframework.web.bind.annotation.RequestBody

interface SellerClient {

    @RequestLine("GET /seller/{id}")
    fun findById(@Param id: String): SellerDTO
}