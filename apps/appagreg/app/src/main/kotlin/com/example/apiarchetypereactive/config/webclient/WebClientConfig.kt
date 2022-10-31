package com.example.apiarchetypereactive.config.webclient

import com.example.apiarchetypereactive.config.Logger
import org.springframework.web.reactive.function.client.WebClient


interface WebClientConfig {

    fun webClientBuilder(): WebClient.Builder {
        return WebClient
            .builder()
            .filter { request, next ->
                Logger.info(
                    "method: {}, url: {}",
                    request.method().name,
                    request.url().toASCIIString()
                )
                next.exchange(request)
            }
    }

}