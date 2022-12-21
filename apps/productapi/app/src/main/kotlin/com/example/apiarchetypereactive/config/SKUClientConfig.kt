package com.example.apiarchetypereactive.config

import com.example.apiarchetypereactive.client.http.SKUClient
import com.example.apiarchetypereactive.config.properties.APIProperties
import feign.Feign
import feign.Logger
import feign.RequestTemplate
import feign.codec.Decoder
import feign.codec.Encoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@Configuration
class SKUClientConfig {

    private val log: org.slf4j.Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun skuClient(
        encoder: Encoder,
        decoder: Decoder,
        errorLogger: Logger,
        level: Logger.Level,
        skuProperties: APIProperties
    ): SKUClient =
        Feign
            .builder()
            .encoder(encoder)
            .decoder(decoder)
            .logger(errorLogger)
            .logLevel(level)
            .requestInterceptor { template: RequestTemplate ->
                log.info("REQUEST:// [{}] {}", template.method(), template.url())
                template.header(
                    HttpHeaders.ACCEPT,
                    MediaType.APPLICATION_JSON_VALUE
                )
                template.header(
                    HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_JSON_VALUE
                )

            }
            .target(SKUClient::class.java, skuProperties.url)

}