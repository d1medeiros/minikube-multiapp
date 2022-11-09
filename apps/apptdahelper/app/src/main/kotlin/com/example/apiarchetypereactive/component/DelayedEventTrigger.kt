package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.extension.isAfterOrEqual
import com.example.apiarchetypereactive.extension.isLimit
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.repository.FullEventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class DelayedEventTrigger(
    private val fullEventRepository: FullEventRepository
) : EventTrigger {
    val Logger: Logger = LoggerFactory.getLogger(javaClass)
    override suspend fun send(
        event: Event,
        lastEvent: Event?,
        now: LocalDateTime,
    ): Event? {
        val frequency = event.frequency
        Logger.debug(
            "event[{}] isAfter:{} dailyEvent.isNull:{}",
            event.id,
            now.isAfterOrEqual(event.dataBase),
            lastEvent == null
        )
        return when {
            event.dataBase.isLimit(now) && lastEvent != null ->
                fullEventRepository.save(
                    lastEvent.copy(
                        id = null,
                        frequency = frequency?.copy(id = null),
                        notebookId = delayedList
                    )
                )

            else -> null

        }
    }
}