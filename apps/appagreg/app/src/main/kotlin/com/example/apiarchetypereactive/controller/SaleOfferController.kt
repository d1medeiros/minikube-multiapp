package com.example.apiarchetypereactive.controller

import com.example.apiarchetypereactive.model.SaleOffer
import com.example.apiarchetypereactive.service.SaleOfferService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sale-offers")
class SaleOfferController(
    private val saleOfferService: SaleOfferService
) {
    val log: Logger = LoggerFactory.getLogger("SaleOffer")

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): SaleOffer  {
        log.info("SaleOfferController.find")
       return saleOfferService.findById(id)
    }


}