package com.example.apiarchetypereactive

import com.example.apiarchetypereactive.config.properties.DataBaseProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(DataBaseProperties::class)
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
