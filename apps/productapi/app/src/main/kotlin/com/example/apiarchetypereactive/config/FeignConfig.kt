package com.example.apiarchetypereactive.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

    @Bean
    fun encoder(mapper: ObjectMapper): Encoder {
        return JacksonEncoder(mapper)
    }

    @Bean
    fun decoder(mapper: ObjectMapper): Decoder {
        return JacksonDecoder(mapper)
    }

    @Bean
    fun errorLogger(): Logger {
        return Slf4jLogger()
    }

    @Bean
    fun level(): Logger.Level {
        return Logger.Level.FULL
    }

}