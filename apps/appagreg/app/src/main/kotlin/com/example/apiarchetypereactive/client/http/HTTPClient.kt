package com.example.apiarchetypereactive.client.http

import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange

interface HTTPClient {
    fun getWebClient(): WebClient



}