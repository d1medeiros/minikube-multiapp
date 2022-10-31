package com.example.apiarchetypereactive.config.webclient

import com.example.apiarchetypereactive.config.properties.APIProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class VerifyClientConfig(
    private val verifyProperties: APIProperties
): WebClientConfig {

    @Bean
    fun verifyWebClient(): WebClient {
        return webClientBuilder()
            .baseUrl(verifyProperties.hostname)
            .build()

    }

}