package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Notebook
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TimeControl(
    private val eventMap: Sequence<EventTrigger>
) {
    suspend fun run(dateTime: LocalDateTime, event: Event, notebookId: Long) {
        Logger.info("time: {} event: {}", dateTime, event.label)
        var lastEvent: Event? = null
        for (eventTrigger in eventMap) {
            lastEvent = eventTrigger.send(event, lastEvent, dateTime)
        }
    }
}


fun List<Notebook>.notebook(id: Long): Notebook {
    return this.first { it.id == id }
}