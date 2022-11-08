package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.extension.getNextDateTime
import com.example.apiarchetypereactive.extension.isAfterOrEqual
import com.example.apiarchetypereactive.extension.isLimit
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.repository.FullEventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

interface EventTrigger {
    suspend fun send(
        event: Event,
        lastEvent: Event?,
        now: LocalDateTime,
    ): Event?

}

const val defaultList = 1L
const val dailyList = 2L
const val delayedList = 3L

class DefaultEventTrigger(
    private val fullEventRepository: FullEventRepository
) : EventTrigger {
    val Logger: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun send(
        event: Event,
        lastEvent: Event?,
        now: LocalDateTime,
    ): Event? {
        val frequency = event.frequency
        val newDate = frequency.getNextDateTime(event.dataBase)
        val dailyEvent = fullEventRepository
            .findByNotebookIdAndLabel(dailyList, event.label)
        val afterOrEqual = now.isAfterOrEqual(event.dataBase)
        Logger.info(
            "\nevent[{}] isAfter[{}]:\n{}\n{} \ndailyEvent.isNull:{}\n",
            event.id,
            afterOrEqual,
            now,
            event.dataBase,
            dailyEvent == null
        )
        return when {
            afterOrEqual && dailyEvent == null -> {
                Logger.info("afterOrEqual && dailyEvent == null")
                fullEventRepository.save(
                    event.copy(
                        dataBase = newDate,
                        frequency = null,
                    )
                )
                return dailyEvent
            }

            dailyEvent != null -> {
                Logger.info("dailyEvent != null")
                dailyEvent
            }
            else -> {
                Logger.info("else")
                null
            }
        }
    }

}

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
        Logger.info(
            "\nevent[{}] isAfter:{} dailyEvent.isNull:{}\n",
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

@Configuration
class EventConfig(
    private val fullEventRepository: FullEventRepository
) {

    @Bean
    fun eventMap(): List<EventTrigger> {
        return arrayListOf(
            DefaultEventTrigger(fullEventRepository),
            DailyEventTrigger(fullEventRepository),
            DelayedEventTrigger(fullEventRepository),
        )
    }
}