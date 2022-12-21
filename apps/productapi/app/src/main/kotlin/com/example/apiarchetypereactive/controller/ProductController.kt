package com.example.apiarchetypereactive.controller

import com.example.apiarchetypereactive.model.Product
import com.example.apiarchetypereactive.model.SaleOffer
import com.example.apiarchetypereactive.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("product")
class ProductController(
    private val productService: ProductService
) {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    suspend fun find(): Set<Product> {
        log.info("buscando todos produtos")
        return productService.findAll()
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): Product {
        log.info("buscando produto $id")
        return productService.findById(id)
    }


}