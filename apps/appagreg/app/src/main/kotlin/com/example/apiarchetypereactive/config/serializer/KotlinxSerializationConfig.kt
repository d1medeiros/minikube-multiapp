package com.example.apiarchetypereactive.config.serializer

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KotlinxSerializationConfig {

    @Bean
    fun json() = Json {
        ignoreUnknownKeys = true

    }
}