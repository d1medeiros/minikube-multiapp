package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.model.Product
import com.example.apiarchetypereactive.repository.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductComponent(
    private val productRepository: ProductRepository
) {
    fun findById(id: String): Product {
        return productRepository.getById(id)
            ?: throw RuntimeException("not found product $id")
    }

    fun findAll(): Set<Product> {
        return productRepository.getAll()
    }


}