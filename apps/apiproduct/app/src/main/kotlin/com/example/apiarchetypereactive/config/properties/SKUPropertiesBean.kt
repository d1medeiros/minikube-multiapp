package com.example.apiarchetypereactive.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SKUPropertiesBean {

    @Bean
    @ConfigurationProperties("api.sku")
    fun skuProperties(): APIProperties {
        return APIProperties()
    }
}