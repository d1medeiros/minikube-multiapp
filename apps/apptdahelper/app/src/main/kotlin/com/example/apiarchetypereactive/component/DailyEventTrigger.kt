package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.extension.isAfterOrEqual
import com.example.apiarchetypereactive.extension.isLimit
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.repository.FullEventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class DailyEventTrigger(
    private val fullEventRepository: FullEventRepository
) : EventTrigger {
    val Logger: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun send(
        event: Event,
        lastEvent: Event?,
        now: LocalDateTime,
    ): Event? {
        val frequency = event.frequency
        val afterOrEqual = now.isAfterOrEqual(event.dataBase)
        Logger.info(
            "\nevent[{}] isAfter[{}]:\n{}\n{} \ndailyEvent.isNull:{}\n",
            event.id,
            afterOrEqual,
            now,
            event.dataBase,
            lastEvent == null,
        )
        return when {
            event.dataBase.isLimit(now) && lastEvent != null -> {
                Logger.info("event.dataBase.isLimit(now)")
                fullEventRepository.delete(lastEvent)
                return lastEvent
            }

            afterOrEqual && lastEvent == null -> {
                Logger.info("afterOrEqual && lastEvent == null")
                fullEventRepository.save(
                    event.copy(
                        id = null,
                        frequency = frequency?.copy(id = null),
                        notebookId = dailyList
                    )
                )
            }
            lastEvent != null -> {
                Logger.info("lastEvent != null")
                lastEvent
            }
            else -> {
                Logger.info("else")
                null
            }
        }
    }
}