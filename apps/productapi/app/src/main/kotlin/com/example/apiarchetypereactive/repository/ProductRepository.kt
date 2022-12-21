package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Product
import com.example.apiarchetypereactive.model.SellerDTO
import com.example.apiarchetypereactive.model.SkuDTO
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProductRepository {

    private val productList = setOf(
        Product(
            id = "1",
            name = "notebook",
            skus = setOf(
                SkuDTO(
                    id = "1"
                )
            ),
            sellers = setOf(
                SellerDTO(
                    id = "1"
                )
            )
        ),
        Product(
            id = "2",
            name = "iphone",
            skus = setOf(
                SkuDTO(
                    id = "2"
                ),
                SkuDTO(
                    id = "3"
                ),
            ),
            sellers = setOf(
                SellerDTO(
                    id = "1"
                )
            )
        ),
        Product(
            id = "3",
            name = "tv sony",
            skus = setOf(
                SkuDTO(
                    id = "4"
                )
            ),
            sellers = setOf(
                SellerDTO(
                    id = "2"
                )
            )
        ),
        Product(
            id = "4",
            name = "notebook",
            skus = setOf(
                SkuDTO(
                    id = "5"
                )
            ),
            sellers = setOf(
                SellerDTO(
                    id = "3"
                )
            )
        ),
    )

    fun getById(id: String): Product? {
       return productList.firstOrNull { it.id == id }
    }

    fun getAll(): Set<Product> {
        return productList
    }
}