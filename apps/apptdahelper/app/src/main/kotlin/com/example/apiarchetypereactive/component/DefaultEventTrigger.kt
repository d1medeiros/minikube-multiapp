package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.extension.getNextDateTime
import com.example.apiarchetypereactive.extension.isAfterOrEqual
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.repository.FullEventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

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