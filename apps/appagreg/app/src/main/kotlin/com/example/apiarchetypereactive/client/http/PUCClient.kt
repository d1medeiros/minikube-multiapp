package com.example.apiarchetypereactive.client.http

import com.example.apiarchetypereactive.model.dto.CustomerOfferDTO
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class PUCClient(
    private val pucWebClient: WebClient
) {
    val log = LoggerFactory.getLogger(javaClass)


    fun getById(headers: MultiValueMap<String, String>, id: String): Mono<CustomerOfferDTO> {
        return pucWebClient
            .get()
            .uri("/customer-offers/$id")
            .headers { it.addAll(headers) }
            .retrieve()
            .bodyToMono(CustomerOfferDTO::class.java)
    }

    suspend fun awaitGetById(headers: HttpHeaders, id: String): CustomerOfferDTO {
        return getById(headers, id)
            .awaitSingle()
    }



}