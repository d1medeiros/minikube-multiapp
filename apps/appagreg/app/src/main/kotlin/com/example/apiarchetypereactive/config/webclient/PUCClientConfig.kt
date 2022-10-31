package com.example.apiarchetypereactive.config.webclient

import com.example.apiarchetypereactive.config.properties.APIProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class PUCClientConfig(
    private val pucProperties: APIProperties
): WebClientConfig {

    @Bean
    fun pucWebClient(): WebClient {
        return webClientBuilder()
            .baseUrl(pucProperties.hostname)
            .build()

    }

}