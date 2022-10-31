package com.example.apiarchetypereactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class ApiArchetypeReactiveApplication

fun main(args: Array<String>) {
    runApplication<ApiArchetypeReactiveApplication>(*args)
}
