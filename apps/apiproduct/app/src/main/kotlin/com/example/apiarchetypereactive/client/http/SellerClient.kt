package com.example.apiarchetypereactive.client.http

import com.example.apiarchetypereactive.model.dto.SellerDTO
import feign.Param
import feign.RequestLine

interface SellerClient {

    @RequestLine("GET /seller/{id}")
    fun findById(@Param id: String): SellerDTO
}