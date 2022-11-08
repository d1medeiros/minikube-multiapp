package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Notebook
import com.example.apiarchetypereactive.repository.REventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TimeControl(
    private val eventMap: List<EventTrigger>
) {
    suspend fun run(dateTime: LocalDateTime, event: Event, notebookId: Long) {
        Logger.info("time: {} event: {}", dateTime, event.label)
        var lastEvent: Event? = null
        for (eventTrigger in eventMap.asSequence()) {
            lastEvent = eventTrigger.send(event, lastEvent, dateTime)
        }
    }
}


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

fun List<Notebook>.notebook(id: Long): Notebook {
    return this.first { it.id == id }
}