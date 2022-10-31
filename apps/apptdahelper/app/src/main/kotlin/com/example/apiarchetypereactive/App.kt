package com.example.apiarchetypereactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableConfigurationProperties
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
