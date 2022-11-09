package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.repository.REventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EventManager(
    private val eventRepository: REventRepository,
    private val timeControl: TimeControl
) {


    // run every 1h
    fun run(now: LocalDateTime) = runBlocking {
        eventRepository.findAll(defaultList)
            .map {
                async {
                    timeControl.run(now, it, defaultList)
                }
            }.awaitAll()
    }
}