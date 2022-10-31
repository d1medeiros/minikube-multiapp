package com.example.apiarchetypereactive.service

import com.example.apiarchetypereactive.client.http.PUCClient
import com.example.apiarchetypereactive.client.http.VerifyClient
import com.example.apiarchetypereactive.model.SaleOffer
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class SaleOfferService(
    private val verifyClient: VerifyClient,
    private val PUCClient: PUCClient
) {


    fun findById(id: String): SaleOffer {
        val headers = HttpHeaders()
        headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
        val dtoList = runBlocking {
            val customerOfferDTO = PUCClient.awaitGetById(headers, id)
            val verifyDTO = verifyClient.awaitGetById(headers, id)
            when {
                verifyDTO.valid ->
                    SaleOffer(
                        customerOfferDTO.id,
                        customerOfferDTO.eligible,
                        customerOfferDTO.limit,
                        verifyDTO.internalCode
                    )
                // TODO: create not found exception
                else -> throw Exception("not found")
            }
        }
        return dtoList
    }

}
