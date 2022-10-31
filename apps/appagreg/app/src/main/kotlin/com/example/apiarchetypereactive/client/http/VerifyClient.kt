package com.example.apiarchetypereactive.client.http

import com.example.apiarchetypereactive.model.dto.VerifyDTO
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class VerifyClient(
    private val verifyWebClient: WebClient
) {
    val log = LoggerFactory.getLogger(javaClass)

    fun getById(headers: MultiValueMap<String, String>, id: String): Mono<VerifyDTO> {
        return verifyWebClient
            .get()
            .uri("/verify/$id")
            .headers { it.addAll(headers) }
            .retrieve()
            .bodyToMono(VerifyDTO::class.java)
    }

    suspend fun awaitGetById(headers: HttpHeaders, id: String): VerifyDTO {
        return getById(headers, id)
            .awaitSingle()
    }

}