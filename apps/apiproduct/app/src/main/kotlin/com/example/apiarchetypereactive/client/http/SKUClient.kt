package com.example.apiarchetypereactive.client.http

import com.example.apiarchetypereactive.model.dto.SkuDTO
import feign.Param
import feign.RequestLine

interface SKUClient {

    @RequestLine("GET /sku/{id}")
    fun findById(@Param id: String): SkuDTO
}