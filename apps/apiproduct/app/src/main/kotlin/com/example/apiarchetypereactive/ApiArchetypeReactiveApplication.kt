package com.example.apiarchetypereactive

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component



@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
class ApiArchetypeReactiveApplication

fun main(args: Array<String>) {

    runApplication<ApiArchetypeReactiveApplication>(*args)
}


@Component
class ScheduledTasks {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    @Scheduled(fixedRate = 3000)
    fun reportCurrentTime() {
        log.info("The time is now")
    }


}